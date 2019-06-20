package credit.core.service;

import credit.core.exception.CreditRuntimeException;
import credit.core.transformer.AccountDtoTransformer;
import credit.core.transformer.CouponDtoTransformer;
import credit.core.transformer.PoolDtoTransformer;
import credit.core.utl.TokenGenerator;
import credit.db.entity.Account;
import credit.db.entity.Coupon;
import credit.db.entity.Item;
import credit.db.entity.Pool;
import credit.db.repository.AccountRepository;
import credit.db.repository.CouponRepository;
import credit.db.repository.ItemRepository;
import credit.db.repository.PoolRepository;
import credit.model.CouponDto;
import credit.model.ItemDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final CouponRepository couponRepository;

    private final PoolRepository poolRepository;

    private final ItemRepository itemRepository;

    private final AccountRepository accountRepository;

    private final CouponDtoTransformer couponDtoTransformer;

    private final PoolDtoTransformer poolDtoTransformer;

    private final AccountDtoTransformer accountDtoTransformer;

    public CouponService(final CouponRepository couponRepository,
                         final PoolRepository poolRepository,
                         final ItemRepository itemRepository,
                         final AccountRepository accountRepository,
                         final CouponDtoTransformer couponDtoTransformer,
                         final PoolDtoTransformer poolDtoTransformer,
                         final AccountDtoTransformer accountDtoTransformer) {
        this.couponRepository = couponRepository;
        this.poolRepository = poolRepository;
        this.itemRepository = itemRepository;
        this.accountRepository = accountRepository;
        this.couponDtoTransformer = couponDtoTransformer;
        this.poolDtoTransformer = poolDtoTransformer;
        this.accountDtoTransformer = accountDtoTransformer;
    }

    @Cacheable(value = "coupon",
               key = "#publicId")
    public CouponDto getCoupon(final String publicId) {
        Coupon coupon = couponRepository.findFirstByPublicId(TokenGenerator.stringToId(publicId));
        return couponDtoTransformer.generateSimple(coupon);
    }

    public List<CouponDto> getCoupons(final String accountEditId) {
        Account account = accountRepository.findFirstByEditId(TokenGenerator.stringToId(accountEditId));
        if (account == null) {
            throw new CreditRuntimeException(HttpStatus.BAD_REQUEST, "No account found");
        }
        List<Coupon> coupons = couponRepository.findByAccountPublicId(account.getPublicId());

        List<CouponDto> couponDtos = coupons.stream()
                .map(couponDtoTransformer::generateSimple)
                .collect(Collectors.toList());

        return couponDtos;
    }

    public long getCouponCount(final String accountEditId) {
        Account account = accountRepository.findFirstByEditId(TokenGenerator.stringToId(accountEditId));
        if (account == null) {
            throw new CreditRuntimeException(HttpStatus.BAD_REQUEST, "No account found");
        }
        return couponRepository.countByAccountPublicId(account.getPublicId());
    }

    public ItemDto addCouponToAccount(final String couponPublicId, final String accountEditId) {
        Coupon coupon = couponRepository.findFirstByPublicId(TokenGenerator.stringToId(couponPublicId));
        if (coupon == null) {
            throw new CreditRuntimeException(HttpStatus.BAD_REQUEST, "Coupon code invalid: " + couponPublicId);
        }
        Account account = accountRepository.findOneByEditId(TokenGenerator.stringToId(accountEditId));
        if (account == null) {
            throw new CreditRuntimeException(HttpStatus.BAD_REQUEST, "Account not found: " + accountEditId);
        }

        Item item = new Item(TokenGenerator.generateToken(), coupon.getPool(), account, coupon.getAmount(),
                             coupon.getLevel() + 1, TokenGenerator.generateToken(), coupon.getMessage());

        itemRepository.saveAndFlush(item);
        // TODO recalculate account score

        couponRepository.delete(coupon);
        couponRepository.flush();

        ItemDto itemDto = new ItemDto(item.getId(), item.getPublicId(), item.getCreated(), item.getAmount(),
                                      item.getLevel(), poolDtoTransformer.generate(item.getPool()));
        itemDto.setAccountDto(accountDtoTransformer.generateShallow(account));

        return itemDto;
    }

    public CouponDto generateNewCoupon(final String accountEditId,
                                       final String itemPublicId,
                                       final BigDecimal amount,
                                       final String message) {
        Item item = itemRepository.findOneByPublicId(itemPublicId);
        if (!item.getAccount()
                .getEditId()
                .equals(TokenGenerator.stringToId(accountEditId))) {
            throw new CreditRuntimeException(HttpStatus.UNAUTHORIZED, "Account");
        }

        if (item.getAmount()
                    .compareTo(amount) <= 0) {
            return null;
        }

        Pool pool = item.getPool();

        Coupon coupon = new Coupon();
        setInitialValues(coupon);
        coupon.setPool(pool);
        coupon.setAmount(amount);
        coupon.setLevel(item.getLevel());
        coupon.setMessage(message);
        coupon.setAccountPublicId(item.getAccount()
                                          .getPublicId());
        coupon = couponRepository.saveAndFlush(coupon);

        item.setAmount(item.getAmount()
                               .subtract(coupon.getAmount()));
        if (item.getAmount()
                    .compareTo(BigDecimal.ZERO) == 0) {
            itemRepository.delete(item);
            itemRepository.flush();
        } else {
            itemRepository.saveAndFlush(item);
        }

        return couponDtoTransformer.generate(coupon);
    }

    private void setInitialValues(Coupon coupon) {
        coupon.setPublicId(TokenGenerator.generateToken());
    }
}
