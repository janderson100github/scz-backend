package credit.db.repository;

import credit.db.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Item findOneByPublicId(String publicId);

    Item findFirstByPublicId(String couponPublicId);

    Item findOneByTransactionId(String transactionId);
}
