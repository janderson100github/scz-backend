package credit.core.transformer;

import credit.db.entity.Verification;
import credit.model.VerificationDto;
import org.springframework.stereotype.Component;

@Component
public class VerificationDtoTransformer {

    public VerificationDtoTransformer() {
    }

    public VerificationDto transform(Verification verification) {
        return new VerificationDto(verification.getPoolPublicId(), verification.getCode(), verification.getEmail(),
                                   verification.getDomain(), verification.getInfo());
    }

    public Verification transform(VerificationDto verificationDto) {
        Verification verification = new Verification();
        verification.setPoolPublicId(verificationDto.getPoolPublicId());
        verification.setEmail(verificationDto.getEmail());
        verification.setDomain(verificationDto.getDomain());
        verification.setAllowHtml(verificationDto.getAllowHtml());
        return verification;
    }
}
