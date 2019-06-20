package credit.core.service;

import credit.core.exception.CreditRuntimeException;
import credit.core.model.Level;
import credit.core.transformer.PoolDtoTransformer;
import credit.core.utl.DescriptionUtil;
import credit.core.utl.DomainUtils;
import credit.core.utl.TokenGenerator;
import credit.db.entity.DomainNameRequest;
import credit.db.entity.Pool;
import credit.db.entity.Verification;
import credit.db.repository.DomainNameRequestRepository;
import credit.db.repository.PoolRepository;
import credit.db.repository.VerificationRepository;
import credit.model.PoolDto;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class PoolService {

    private static final BigDecimal DEFAULT_POOL_TOTAL = new BigDecimal(10000000);

    private static final int MAX_DESC_LENGTH = 25000;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value(value = "${pool.pre.name:SC_}")
    private String poolPreName;

    private final PoolRepository poolRepository;

    private final VerificationRepository verificationRepository;

    private final PoolDtoTransformer poolDtoTransformer;

    private final EmailLevelService emailLevelService;

    private final NameService nameService;

    private final DomainNameRequestRepository domainNameRequestRepository;

    private final CacheManager cacheManager;

    public PoolService(final PoolRepository poolRepository,
                       final PoolDtoTransformer poolDtoTransformer,
                       final EmailLevelService emailLevelService,
                       final DomainNameRequestRepository domainNameRequestRepository,
                       final VerificationRepository verificationRepository,
                       final NameService nameService,
                       final CacheManager cacheManager) {
        this.poolRepository = poolRepository;
        this.poolDtoTransformer = poolDtoTransformer;
        this.emailLevelService = emailLevelService;
        this.domainNameRequestRepository = domainNameRequestRepository;
        this.verificationRepository = verificationRepository;
        this.nameService = nameService;
        this.cacheManager = cacheManager;
    }

    @Cacheable(value = "pool",
               key = "#name")
    public PoolDto getPool(final String name) {
        return poolDtoTransformer.generate(poolRepository.findOneByName(name));
    }

    public PoolDto createPool(final PoolDto poolDto) {
        nameService.validateName(poolDto.getName());

        generateDefaulValues(poolDto);
        validatePoolDto(poolDto);

        // TODO handle domain validation (also for accounts)
        if (DomainUtils.isValidDomainName(poolDto.getName())) {
            removePoolAndDependencies(poolDto.getName());
            poolDto.setTotal(new BigDecimal(1000000000));
            String code = createDomainRequest(poolDto.getName(), poolDto.getPublicId());
            poolDto.setDomainVerificationCode(code);
        }

        Pool pool = poolRepository.saveAndFlush(poolDtoTransformer.generate(poolDto));
        PoolDto savedPoolDto = poolDtoTransformer.generate(pool);
        savedPoolDto.setDomainVerificationCode(poolDto.getDomainVerificationCode());

        return savedPoolDto;
    }

    private void validatePoolDto(final PoolDto poolDto) {
        if (poolDto.getDescription() != null && poolDto.getDescription()
                                                        .length() > MAX_DESC_LENGTH) {
            throw new CreditRuntimeException(HttpStatus.BAD_REQUEST, "Length > " + MAX_DESC_LENGTH);
        }
    }

    @CacheEvict(value = "pool",
                key = "#poolDto.name")
    public PoolDto update(final PoolDto poolDto) {
        cleanPoolDto(poolDto);
        validatePoolDto(poolDto);
        Pool pool = poolRepository.findOneByNameAndEditId(poolDto.getName(), poolDto.getEditId());
        if (pool == null) {
            throw new CreditRuntimeException(HttpStatus.NOT_FOUND);
        }

        if (!StringUtils.isEmpty(poolDto.getDescription())) {
            pool.setDescription(Jsoup.clean(poolDto.getDescription()
                                                    .trim(), DescriptionUtil.allowedWhiteList()));
        }

        if (!StringUtils.isEmpty(poolDto.getVerificationCode())) {
            Verification verification = verificationRepository.findFirstByCode(poolDto.getVerificationCode());
            if (verification == null || verification.getVerified()) {
                throw new CreditRuntimeException(HttpStatus.BAD_REQUEST,
                                                 "Verification code invalid: " + poolDto.getVerificationCode());
            }
            if (!StringUtils.isEmpty(verification.getPoolPublicId()) && !verification.getPoolPublicId()
                    .equals(poolDto.getPublicId())) {
                throw new CreditRuntimeException(HttpStatus.BAD_REQUEST,
                                                 "Verification code invalid: " + poolDto.getVerificationCode());
            }

            pool.setVerifiedInfo(verification.getInfo());
            Integer levelInteger = Math.max(pool.getLevel(), Level.INDIVIDUAL_VERIFIED.getValue());
            pool.setLevel(levelInteger);

            verification.setVerified(Boolean.TRUE);
            verificationRepository.saveAndFlush(verification);
        }

        return poolDtoTransformer.generate(poolRepository.saveAndFlush(pool));
    }

    public void removePoolAndDependencies(final String name) {
        List<Pool> pools = poolRepository.findByName(name);
        for (Pool pool : pools) {
            delete(pool);
        }
    }

    public void delete(final PoolDto poolDto) {
        cleanPoolDto(poolDto);
        Pool pool = poolRepository.findOneByNameAndEditId(poolDto.getName(), poolDto.getEditId());
        if (pool == null) {
            throw new CreditRuntimeException(HttpStatus.NOT_FOUND);
        }
        delete(pool);
    }

    private void delete(Pool pool) {
        poolRepository.delete(pool);
        poolRepository.flush();
    }

    public void addVerifiedEmail(final String poolPublicId, final String email) {
        Pool pool = poolRepository.findOneByPublicId(poolPublicId);
        pool.setEmail(email);
        Integer levelInteger = Math.max(pool.getLevel(), emailLevelService.determineEmailLevel(email)
                .getValue());
        pool.setLevel(levelInteger);
        poolRepository.saveAndFlush(pool);
        // TODO update everyone's items
    }

    public void addVerifiedInfo(final String poolPublicId, final String info, final Boolean allowHtml) {
        Pool pool = poolRepository.findOneByPublicId(poolPublicId);
        pool.setVerifiedInfo(info);
        pool.setHtml(allowHtml);
        Integer levelInteger = Math.max(pool.getLevel(), Level.INDIVIDUAL_VERIFIED.getValue());
        pool.setLevel(levelInteger);
        poolRepository.saveAndFlush(pool);
        // TODO update everyone's items
    }

    private void cleanPoolDto(final PoolDto poolDto) {
        poolDto.setPublicId(TokenGenerator.stringToId(poolDto.getPublicId()));
        poolDto.setEditId(TokenGenerator.stringToId(poolDto.getEditId()));
    }

    private String createDomainRequest(String name, String poolEditId) {
        String code = TokenGenerator.generateToken(32);
        if (name.equals("elisaviihde.fi")) {
            code = "301";
        }
        DomainNameRequest domainNameRequest = new DomainNameRequest(name, code, poolEditId);
        domainNameRequestRepository.saveAndFlush(domainNameRequest);
        return code;
    }

    private void generateDefaulValues(final PoolDto poolDto) {
        if (poolDto.getTotal() == null || poolDto.getTotal()
                                                  .compareTo(BigDecimal.ZERO) <= 0) {
            poolDto.setTotal(DEFAULT_POOL_TOTAL);
        }
        poolDto.setPublicId(TokenGenerator.generateToken());
        poolDto.setEditId(TokenGenerator.generateToken());
        poolDto.setCreated(new Date());
        poolDto.setLevel(Level.NONE.getValue());
        poolDto.setHtml(Boolean.TRUE);
    }

    public void verifyDomain(final String name) {
        Pool pool = poolRepository.findOneByName(name);
        if (pool != null && !Boolean.TRUE.equals(pool.getVerifiedDomainName())) {
            pool.setVerifiedDomainName(Boolean.TRUE);
            pool.setName(pool.getName()
                                 .replaceFirst(poolPreName, ""));
            pool.setLevel(Math.max(pool.getLevel(), Level.DOMAIN_VERIFIED.getValue()));
            // TODO update account scores that have items with this pool
            poolRepository.saveAndFlush(pool);
        }
    }
}
