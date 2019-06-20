package credit.db.repository;

import credit.db.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findOneByPublicId(String publicId);

    Account findOneByPublicIdAndEditId(String publicId, String editId);

    Account findOneByEditId(String fromAccountEditId);

    Account findOneByAliasAndEditId(String alias, String editId);

    Account findOneByAlias(String alias);

    Account findFirstByAlias(String alias);

    Account findFirstByEditId(String accountEditId);

    List<Account> findByEditIdStartsWithOrderByCreatedDesc(String accountEditId);
}
