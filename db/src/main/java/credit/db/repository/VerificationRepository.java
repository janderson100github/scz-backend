package credit.db.repository;

import credit.db.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {

    Verification findFirstByPoolPublicIdAndCode(String poolPublicId, String code);

    Verification findFirstByPoolPublicIdAndEmailIsNotNull(String poolPublicId);

    Verification findFirstByCode(String code);
}
