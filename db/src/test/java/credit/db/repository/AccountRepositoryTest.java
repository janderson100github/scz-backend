package credit.db.repository;

import credit.db.entity.Account;
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

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {Environment.class})
@ComponentScan({"credit.db"})
@EnableConfigurationProperties
@TestPropertySource(locations = "classpath:db-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AccountRepositoryTest {

    @Autowired
    private PoolRepository poolRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void crudTest() {
        Account account = generateAccount();
        Account retrievedAccount = accountRepository.findOneByPublicId(account.getPublicId());
        assertEquals(account.toString(), retrievedAccount.toString());

        String newDesc = "newDesc";
        retrievedAccount.setDescription(newDesc);
        accountRepository.saveAndFlush(retrievedAccount);

        retrievedAccount = accountRepository.findOneByPublicId(account.getPublicId());
        assertEquals(newDesc, retrievedAccount.getDescription());

        accountRepository.delete(retrievedAccount);

        retrievedAccount = accountRepository.findOneByPublicId(account.getPublicId());
        assertNull(retrievedAccount);
    }

    private Account generateAccount() {
        Account account = new Account("publicId", "editId", "desc", "public", true, Arrays.asList());
        accountRepository.saveAndFlush(account);
        return account;
    }

    private void validatePool(Pool pool, Pool foundPool) {
        assertEquals(pool.getName(), foundPool.getName());
        assertEquals(pool.getPublicId(), foundPool.getPublicId());
        assertEquals(pool.getDescription(), foundPool.getDescription());
    }
}
