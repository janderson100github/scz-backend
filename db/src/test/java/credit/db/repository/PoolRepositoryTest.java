package credit.db.repository;

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

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {Environment.class})
@ComponentScan({"credit.db"})
@EnableConfigurationProperties
@TestPropertySource(locations = "classpath:db-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PoolRepositoryTest {

    @Autowired
    private PoolRepository poolRepository;

    @Test
    public void findOneByName() {
        Pool pool = generatePool();
        Pool foundPool = poolRepository.findOneByName(pool.getName());
        validatePool(pool, foundPool);
    }

    @Test
    public void findOneByNameAndEditId() {
        Pool pool = generatePool();
        Pool foundPool = poolRepository.findOneByNameAndEditId(pool.getName(), pool.getEditId());
        validatePool(pool, foundPool);
    }

    @Test
    public void crudTest() {
        String editId = "editId";
        String publicId = "publicId";
        String name = "name";
        BigDecimal total = new BigDecimal(100);
        String desc = "desc";
        String newDesc = "new desc";

        Pool pool = new Pool(publicId, editId, name, total, desc, 10, null, Boolean.TRUE);

        Pool poolSaved = poolRepository.saveAndFlush(pool);
        assertEquals(new Long(1l), poolSaved.getId());
        assertEquals(publicId, poolSaved.getPublicId());

        poolSaved.setDescription(newDesc);
        poolSaved = poolRepository.saveAndFlush(poolSaved);
        assertEquals(newDesc, poolSaved.getDescription());

        poolRepository.delete(poolSaved);

        assertEquals(0, poolRepository.count());
    }

    private Pool generatePool() {
        Pool pool = new Pool("publicId", "editId", "name", new BigDecimal(100), "desc", 10, null, Boolean.TRUE);
        poolRepository.saveAndFlush(pool);
        return pool;
    }

    private void validatePool(Pool pool, Pool foundPool) {
        assertEquals(pool.getName(), foundPool.getName());
        assertEquals(pool.getPublicId(), foundPool.getPublicId());
        assertEquals(pool.getDescription(), foundPool.getDescription());
    }
}
