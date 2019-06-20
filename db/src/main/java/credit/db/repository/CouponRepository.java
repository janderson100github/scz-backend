package credit.db.repository;

import credit.db.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Coupon findFirstByPublicId(String couponPublicId);

    List<Coupon> findByAccountPublicId(String publicId);

    long countByAccountPublicId(String accountPublicId);
}
