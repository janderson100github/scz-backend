package credit.core;

import credit.core.service.AccountService;
import credit.core.service.ItemService;
import credit.core.service.PoolService;
import credit.core.transformer.AccountDtoTransformer;
import credit.core.transformer.ItemDtoTransformer;
import credit.core.transformer.PoolDtoTransformer;
import credit.db.entity.Item;
import credit.model.AccountDto;
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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {Environment.class, ItemService.class, ItemDtoTransformer.class, PoolService.class,
                                 PoolDtoTransformer.class, AccountService.class, AccountDtoTransformer.class})
@ComponentScan({"credit.db"})
@TestPropertySource(locations = "classpath:core-test.properties")
@EnableConfigurationProperties
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ItemServiceIntegrationTest {

    @Autowired
    private PoolService poolService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ItemService itemService;

    @Test
    public void createNewItem() {
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
        assertNotNull(item.getId());

        // -- get item --
        ItemDto retrievedItem = itemService.getItem(item.getPublicId());
        assertEquals(item.getId(), retrievedItem.getId());

        accountDto = accountService.getAccount(accountDto.getPublicId(), null);
        List<ItemDto> items = accountDto.getItems();
        assertEquals(1, items.size());
    }
}
