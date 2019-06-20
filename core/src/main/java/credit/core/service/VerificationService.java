package credit.core.service;

import credit.core.exception.CreditRuntimeException;
import credit.core.transformer.VerificationDtoTransformer;
import credit.core.utl.TokenGenerator;
import credit.db.entity.Pool;
import credit.db.entity.Verification;
import credit.db.repository.PoolRepository;
import credit.db.repository.VerificationRepository;
import credit.model.VerificationDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class VerificationService {

    private VerificationRepository verificationRepository;

    private VerificationDtoTransformer verificationDtoTransformer;

    private PoolService poolService;

    private PoolRepository poolRepository;

    public VerificationService(final VerificationRepository verificationRepository,
                               final VerificationDtoTransformer verificationDtoTransformer,
                               final PoolService poolService,
                               final PoolRepository poolRepository) {
        this.verificationRepository = verificationRepository;
        this.verificationDtoTransformer = verificationDtoTransformer;
        this.poolService = poolService;
        this.poolRepository = poolRepository;
    }

    public VerificationDto generateEmailVerification(String email, String poolPublicId) {
        Verification verification = verificationRepository.findFirstByPoolPublicIdAndEmailIsNotNull(poolPublicId);
        if (verification != null) {
            if (Boolean.TRUE.equals(verification.getVerified())) {
                throw new CreditRuntimeException(HttpStatus.CONFLICT, "Already verified!");
            } else {
                return verificationDtoTransformer.transform(verification);
            }
        }

        verification = new Verification();
        setDefaultValues(verification);
        verification.setEmail(email);
        verification.setPoolPublicId(poolPublicId);
        verification.setVerified(Boolean.FALSE);
        Verification savedVerification = verificationRepository.saveAndFlush(verification);
        return verificationDtoTransformer.transform(savedVerification);
    }

    public VerificationDto generateInfoVerification(final String info, final String name, final Boolean allowHtml) {
        Verification verification = new Verification();

        String poolName = null;
        if (!StringUtils.isEmpty(name)) {
            Pool pool = poolRepository.findFirstByName(name);
            if (pool == null) {
                throw new CreditRuntimeException(HttpStatus.BAD_REQUEST, "Pool not found: " + name);
            }
            verification.setPoolPublicId(pool.getPublicId());
            poolName = pool.getName();
        }
        setDefaultValues(verification);
        verification.setInfo(info);
        if (Boolean.TRUE.equals(allowHtml)) {
            verification.setAllowHtml(allowHtml);
        }
        verification.setVerified(Boolean.FALSE);
        Verification savedVerification = verificationRepository.saveAndFlush(verification);
        VerificationDto dto = verificationDtoTransformer.transform(savedVerification);
        dto.setPoolName(poolName);
        return dto;
    }

    public VerificationDto verify(String poolPublicId, String code) {
        Verification verification = verificationRepository.findFirstByPoolPublicIdAndCode(poolPublicId, code);
        if (verification != null) {
            verification.setVerified(Boolean.TRUE);
            verificationRepository.saveAndFlush(verification);

            if (!StringUtils.isEmpty(verification.getInfo())) {
                poolService.addVerifiedInfo(poolPublicId, verification.getInfo(), verification.getAllowHtml());
            } else {
                poolService.addVerifiedEmail(poolPublicId, verification.getEmail());
            }

            return verificationDtoTransformer.transform(verification);
        }
        throw new CreditRuntimeException(HttpStatus.NOT_FOUND);
    }

    private void setDefaultValues(final Verification verification) {
        String code = TokenGenerator.generateToken();
        while (verificationRepository.findFirstByCode(code) != null) {
            code = TokenGenerator.generateToken();
        }
        verification.setCode(TokenGenerator.generateToken());
    }
}
