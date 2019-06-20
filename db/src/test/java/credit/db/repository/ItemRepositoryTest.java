package credit.db.repository;

import credit.db.entity.Account;
import credit.db.entity.Item;
import credit.db.entity.Pool;
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
import java.util.Arrays;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {Environment.class})
@ComponentScan({"credit.db"})
@EnableConfigurationProperties
@TestPropertySource(locations = "classpath:db-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ItemRepositoryTest {

    @Autowired
    private PoolRepository poolRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void crudTest() {
        Item item = generateItem();
        Item retrievedItem = itemRepository.findOneByPublicId(item.getPublicId());
        assertEquals(item.toString(), retrievedItem.toString());

        BigDecimal newAmount = new BigDecimal(99);
        retrievedItem.setAmount(newAmount);
        retrievedItem = itemRepository.findOneByPublicId(item.getPublicId());
        assertEquals(newAmount, retrievedItem.getAmount());

        itemRepository.delete(retrievedItem);
        retrievedItem = itemRepository.findOneByPublicId(item.getPublicId());
        assertNull(retrievedItem);
    }

    private Item generateItem() {
        Pool pool = generatePool();
        Account account = generateAccount();
        Item item = new Item("publicId", pool, account, BigDecimal.TEN, 0, "transactionId", "message");
        itemRepository.saveAndFlush(item);
        return item;
    }

    private Account generateAccount() {
        Account account = new Account("publicId", "editId", "desc", "public", true, Arrays.asList());
        accountRepository.saveAndFlush(account);
        return account;
    }

    private Pool generatePool() {
        Pool pool = new Pool("publicId", "editId", "prefix", new BigDecimal(100), "desc", 10, null, Boolean.TRUE);
        poolRepository.saveAndFlush(pool);
        return pool;
    }

    private void validatePool(Pool pool, Pool foundPool) {
        assertEquals(pool.getName(), foundPool.getName());
        assertEquals(pool.getPublicId(), foundPool.getPublicId());
        assertEquals(pool.getDescription(), foundPool.getDescription());
    }
}
