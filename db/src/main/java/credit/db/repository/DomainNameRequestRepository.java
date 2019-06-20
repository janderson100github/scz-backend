package credit.db.repository;

import credit.db.entity.Domain;
import credit.db.entity.DomainNameRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainNameRequestRepository extends JpaRepository<DomainNameRequest, Long> {

    Domain findOneByPrefix(String prefix);

    Domain findOneByCode(String code);
}
