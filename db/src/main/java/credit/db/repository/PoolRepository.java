package credit.db.repository;

import credit.db.entity.Pool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PoolRepository extends JpaRepository<Pool, Long> {

    Pool findOneByName(String name);

    Pool findOneByNameAndEditId(String name, String editId);

    Pool findOneByPublicId(String poolPublicId);

    List<Pool> findByName(String name);

    Pool findFirstByPublicId(String poolPublicId);

    Pool findFirstByName(String name);
}
