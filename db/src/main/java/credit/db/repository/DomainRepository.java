package credit.db.repository;

import credit.db.entity.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainRepository extends JpaRepository<Domain, Long> {

    Domain findOneByDomain(String domain);
}
