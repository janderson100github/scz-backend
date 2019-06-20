package credit.core.service;

import credit.db.entity.DomainNameRequest;
import credit.db.repository.DomainNameRequestRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@Service
public class DomainPrefixRequestService {

    private final RestTemplate restTemplate = new RestTemplate();

    private DomainNameRequestRepository domainNameRequestRepository;

    private PoolService poolService;

    public DomainPrefixRequestService(final DomainNameRequestRepository domainNameRequestRepository,
                                      final PoolService poolService) {
        this.domainNameRequestRepository = domainNameRequestRepository;
        this.poolService = poolService;
    }

    @Scheduled(initialDelay = 0,
               fixedDelay = 1 * 60 * 60 * 1000)
    private void validateRequests() {
        List<DomainNameRequest> requests = domainNameRequestRepository.findAll();
        for (DomainNameRequest dpr : requests) {
            if (isStale(dpr.getCreated())) {
                deleteRequest(dpr);
                poolService.removePoolAndDependencies(dpr.getPrefix());
            } else {
                checkRequest(dpr);
            }
        }
    }

    private void checkRequest(final DomainNameRequest dpr) {
        // TODO follow redirects
        String url = "http://" + dpr.getPrefix();
        try {
            final String response = restTemplate.getForObject(url, String.class);
            if (response.contains(dpr.getCode())) {
                verifyDomainPrefixRequest(dpr);
                deleteRequest(dpr);
            }
        } catch (Exception e) {
            // do nothing
        }
    }

    private void verifyDomainPrefixRequest(final DomainNameRequest dpr) {
        poolService.verifyDomain(dpr.getPrefix());
    }

    private boolean isStale(final Date created) {
        long expires = created.getTime() + 12 * 60 * 60 * 1000;
        return ((new Date()).getTime() - expires) > 0;
    }

    private void deleteRequest(DomainNameRequest dpr) {
        domainNameRequestRepository.delete(dpr);
        domainNameRequestRepository.flush();
    }
}
