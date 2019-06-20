package credit.core.service;

import credit.core.model.Level;
import credit.db.entity.Domain;
import credit.db.repository.DomainRepository;
import org.springframework.stereotype.Service;

@Service
public class EmailLevelService {

    private DomainRepository domainRepository;

    public EmailLevelService(final DomainRepository domainRepository) {
        this.domainRepository = domainRepository;
    }

    public Level determineEmailLevel(String email) {
        if (isCorporateEmail(email)) {
            return Level.EMAIL_VERIFIED_CORPORATE;
        }
        return Level.EMAIL_VERIFIED_BASIC;
    }

    private boolean isCorporateEmail(final String email) {
        Domain domain = domainRepository.findOneByDomain(getEmailDomain(email));
        if (domain != null) {
            return true;
        }
        return false;
    }

    public static String getEmailDomain(final String email) {
        if (email == null || email.isEmpty()) {
            return null;
        }
        int begin = email.indexOf('@') + 1;
        if (begin != -1) {
            int end = email.lastIndexOf('.');
            if (end != -1) {
                return email.substring(begin, end);
            }
        }
        return null;
    }
}
