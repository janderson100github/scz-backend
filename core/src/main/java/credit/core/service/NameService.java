package credit.core.service;

import credit.core.exception.CreditRuntimeException;
import credit.core.utl.DomainUtils;
import credit.core.utl.NameUtils;
import credit.db.entity.Account;
import credit.db.entity.Pool;
import credit.db.repository.AccountRepository;
import credit.db.repository.PoolRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class NameService {

    private AccountRepository accountRepository;

    private PoolRepository poolRepository;

    public NameService(final AccountRepository accountRepository, final PoolRepository poolRepository) {
        this.accountRepository = accountRepository;
        this.poolRepository = poolRepository;
    }

    public void validateName(String name) {
        NameUtils.validateName(name);
        if (DomainUtils.isValidDomainName(name)) {
            throw new CreditRuntimeException(HttpStatus.BAD_REQUEST, "Name cannot be a domain w/o verification.");
        }

        isAccountNameValid(name);
        isPoolNameValid(name);
    }

    private void isAccountNameValid(final String alias) {
        Account account = accountRepository.findFirstByAlias(alias);
        if (account != null) {
            throw new CreditRuntimeException(HttpStatus.BAD_REQUEST, "Name unavailable.");
        }
    }

    private void isPoolNameValid(final String name) {
        Pool pool = poolRepository.findFirstByName(name);
        if (pool != null) {
            throw new CreditRuntimeException(HttpStatus.BAD_REQUEST, "Name unavailable.");
        }
    }
}
