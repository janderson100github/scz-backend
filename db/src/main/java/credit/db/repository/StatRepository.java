package credit.db.repository;

import credit.db.entity.Stat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatRepository extends JpaRepository<Stat, Long> {

    Stat findOneByIpAndItemAndInfo(String ip, String item, String info);
}
