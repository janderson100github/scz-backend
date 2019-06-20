package credit.core;

import credit.core.service.AccountService;
import credit.core.service.CouponService;
import credit.core.service.EmailLevelService;
import credit.core.service.ItemService;
import credit.core.service.PoolService;
import credit.core.transformer.AccountDtoTransformer;
import credit.core.transformer.CouponDtoTransformer;
import credit.core.transformer.ItemDtoTransformer;
import credit.core.transformer.PoolDtoTransformer;
import credit.db.entity.Item;
import credit.model.AccountDto;
import credit.model.CouponDto;
import credit.model.ItemDto;
import credit.model.PoolDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {Environment.class, CouponService.class, CouponDtoTransformer.class, PoolService.class,
                                 PoolDtoTransformer.class, AccountService.class, AccountDtoTransformer.class,
                                 EmailLevelService.class, ItemDtoTransformer.class, ItemService.class})
@ComponentScan({"credit.db"})
@TestPropertySource(locations = "classpath:core-test.properties")
@EnableConfigurationProperties
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CouponServiceIntegrationTest {

    @Autowired
    private PoolService poolService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CouponService couponService;

    @Test
    public void createNewCoupon() {
        // -- pool --
        PoolDto poolDto = new PoolDto(null, null, null, null, "prefix", null, "desc");
        poolDto = poolService.createPool(poolDto);
        assertNotNull(poolDto.getId());
        assertNotNull(poolDto.getPublicId());

        // -- account --
        AccountDto accountDto = new AccountDto(null, null, null, null, "desc", null, 0);
        accountDto = accountService.createAccount(accountDto, "ip");
        assertEquals("public", accountDto.getVisibility());

        // -- item --
        BigDecimal itemAmount = new BigDecimal("12.34");
        Item item = itemService.addNewItemToAccount(poolDto.getName(), accountDto.getPublicId(), itemAmount);

        // -- coupon generate --
        // final String itemPublicId, final BigDecimal amount, final String message
        BigDecimal couponAmount = new BigDecimal("12.00");
        String msg = "msg";
        CouponDto couponDto = couponService.generateNewCoupon(accountDto.getPublicId(), item.getPublicId(),
                                                              couponAmount, msg);
        assertNotNull(couponDto.getPublicId());
        assertEquals(couponDto.getMessage(), msg);

        // -- get item again --
        ItemDto itemAfter = itemService.getItem(item.getPublicId());
        assertEquals(itemAmount.subtract(couponAmount), itemAfter.getAmount());

        // -- add coupon --
        ItemDto itemDto = couponService.addCouponToAccount(couponDto.getPublicId(), accountDto.getPublicId());
        assertEquals(couponAmount, itemDto.getAmount());

        // FIXME these fail cause transactions aren't flushed for some reason?
        accountDto = accountService.getAccount(accountDto.getPublicId(), null);
        assertTrue(itemWithAmountExists(accountDto, itemAmount.subtract(couponAmount)));
        assertTrue(itemWithAmountExists(accountDto, couponAmount));
    }

    private boolean itemWithAmountExists(final AccountDto accountDto, final BigDecimal amount) {
        for (ItemDto itemDto : accountDto.getItems()) {
            System.out.println("XXX " + itemDto);
            if (itemDto.getAmount()
                    .equals(amount)) {
                return true;
            }
        }
        return false;
    }
}
