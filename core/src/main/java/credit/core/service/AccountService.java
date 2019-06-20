package credit.core.service;

import credit.core.exception.CreditRuntimeException;
import credit.core.model.Level;
import credit.core.transformer.AccountDtoTransformer;
import credit.core.utl.DescriptionUtil;
import credit.core.utl.TokenGenerator;
import credit.db.entity.Account;
import credit.db.entity.Item;
import credit.db.entity.Verification;
import credit.db.repository.AccountRepository;
import credit.db.repository.VerificationRepository;
import credit.model.AccountDto;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountService {

    private static final int MAX_DESC_LENGTH = 25000;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AccountRepository accountRepository;

    private final AccountDtoTransformer accountDtoTransformer;

    private final NameService nameService;

    private final VerificationRepository verificationRepository;

    private final StatService statService;

    public AccountService(final AccountRepository accountRepository,
                          final AccountDtoTransformer accountDtoTransformer,
                          final NameService nameService,
                          final VerificationRepository verificationRepository,
                          final StatService statService) {
        this.accountRepository = accountRepository;
        this.accountDtoTransformer = accountDtoTransformer;
        this.nameService = nameService;
        this.verificationRepository = verificationRepository;
        this.statService = statService;
    }

    public AccountDto getAccount(final String publicId, final String editId) {
        if (editId != null) {
            Account account = accountRepository.findOneByPublicIdAndEditId(publicId, editId);
            return accountDtoTransformer.generate(account);
        } else {
            Account account = accountRepository.findOneByPublicId(publicId);
            if (account == null || "private".equals(account.getVisibility())) {
                return null;
            }
            AccountDto dto = accountDtoTransformer.generate(account); // FIXME generatePublic
            dto.setEditId(null);
            return dto;
        }
    }

    @Cacheable(value = "account",
               key = "#alias")
    public AccountDto getAccountByAlias(final String alias) {
        Account account = accountRepository.findOneByAlias(alias);
        if (account == null || "private".equals(account.getVisibility())) {
            return null;
        }
        AccountDto dto = accountDtoTransformer.generate(account);
        dto.setEditId(null);
        return dto;
    }

    public AccountDto createAccount(final AccountDto accountDto, final String ip) {
        generateDefaulValues(accountDto);
        validateAccountDto(accountDto);
        if (!StringUtils.isEmpty(accountDto.getAlias())) {
            nameService.validateName(accountDto.getAlias());
        }

        statService.insertStat(ip, "createAccount", accountDto.getEditId());

        Account account = accountRepository.saveAndFlush(accountDtoTransformer.generate(accountDto));
        return accountDtoTransformer.generate(account);
    }

    public List<String> getRecentAccounts(final String startsWith) {
        List<Account> accounts = accountRepository.findByEditIdStartsWithOrderByCreatedDesc(startsWith);
        List<String> stringList = new ArrayList<>();
        for (Account account : accounts) {
            String line = account.getEditId();
            stringList.add(line);
        }
        return stringList;
    }

    private void validateAccountDto(final AccountDto accountDto) {
        if (accountDto.getDescription() != null && accountDto.getDescription()
                                                           .length() > MAX_DESC_LENGTH) {
            throw new CreditRuntimeException(HttpStatus.BAD_REQUEST, "Length > " + MAX_DESC_LENGTH);
        }
    }

    public AccountDto createAccount(final String ip) {
        return createAccount(new AccountDto(), ip);
    }

    @CacheEvict(value = "account",
                key = "#accountDto.alias")
    public AccountDto update(final AccountDto accountDto) {
        cleanAccountDto(accountDto);
        validateAccountDto(accountDto);
        Account account = accountRepository.findOneByEditId(accountDto.getEditId());
        if (account == null) {
            throw new CreditRuntimeException(HttpStatus.NOT_FOUND);
        }

        if (!StringUtils.isEmpty(accountDto.getDescription())) {
            account.setDescription(Jsoup.clean(accountDto.getDescription()
                                                       .trim(), DescriptionUtil.allowedWhiteList()));
        }
        if (!StringUtils.isEmpty(accountDto.getVisibility())) {
            account.setVisibility(accountDto.getVisibility());
        }
        if (!StringUtils.isEmpty(accountDto.getVerificationId())) {
            Verification verification = verificationRepository.findFirstByCode(accountDto.getVerificationId());
            if (verification == null || verification.getVerified()) {
                throw new CreditRuntimeException(HttpStatus.BAD_REQUEST,
                                                 "Verification code invalid: " + accountDto.getVerificationId());
            }
            account.setHtml(verification.getAllowHtml());
            account.setVerifiedInfo(verification.getInfo());

            accountRepository.saveAndFlush(account);

            verification.setVerified(Boolean.TRUE);
            verificationRepository.saveAndFlush(verification);
        }

        return accountDtoTransformer.generate(accountRepository.saveAndFlush(account));
    }

    public void delete(final AccountDto accountDto) {
        cleanAccountDto(accountDto);
        Account account = accountRepository.findOneByEditId(accountDto.getEditId());
        if (account == null) {
            throw new CreditRuntimeException(HttpStatus.NOT_FOUND);
        }

        accountRepository.delete(account);
        accountRepository.flush();
    }

    public void compressItems(String accountPublicId) {
        // TODO compress and return List<TransactionDto> { created, publicId, message[0:50] }
        // save to compression history?
    }

    public Integer calculateAndSaveAccountScore(Account account) {
        Map<Level, Integer> scoreCard = generateLevelScoreCard();

        for (Item item : account.getItems()) {
            Integer poolLevelAsInt = item.getPool()
                    .getLevel();
            Level poolLevel = Level.getLevel(poolLevelAsInt);
            Integer itemLevel = item.getLevel();
            BigDecimal itemLevelPercent = BigDecimal.ONE.divide(new BigDecimal(itemLevel), 2, RoundingMode.HALF_UP);
            Integer maxForLevel = poolLevel.getMaxAccountScore();
            Integer currentScore = new BigDecimal(poolLevelAsInt).multiply(itemLevelPercent)
                    .intValue();
            Integer totalScore = Math.min(currentScore + scoreCard.get(poolLevel), maxForLevel);
            scoreCard.put(poolLevel, totalScore);
        }

        Integer score = scoreCard.values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();

        account.setScore(score);
        accountRepository.saveAndFlush(account);

        return score;
    }

    private Map<Level, Integer> generateLevelScoreCard() {
        Map<Level, Integer> scoreCard = new HashMap<>();

        for (Level level : Level.values()) {
            scoreCard.put(level, 0);
        }

        return scoreCard;
    }

    private void generateDefaulValues(final AccountDto accountDto) {
        accountDto.setPublicId(TokenGenerator.generateToken());
        accountDto.setEditId(TokenGenerator.generateToken());
        accountDto.setCreated(new Date());
        if (StringUtils.isEmpty(accountDto.getAlias())) {
            accountDto.setAlias(null);
        }
        accountDto.setHtml(Boolean.TRUE);
    }

    private void cleanAccountDto(final AccountDto accountDto) {
        accountDto.setEditId(TokenGenerator.stringToId(accountDto.getEditId()));
        accountDto.setPublicId(TokenGenerator.stringToId(accountDto.getPublicId()));
    }
}
