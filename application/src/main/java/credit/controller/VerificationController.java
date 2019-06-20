package credit.controller;

import credit.Paths;
import credit.core.email.EmailService;
import credit.core.exception.CreditRuntimeException;
import credit.core.service.VerificationService;
import credit.model.VerificationDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = Paths.API + Paths.VERIFICATION,
                produces = MediaType.APPLICATION_JSON_VALUE)
@Api(description = "Verification endpoints",
     tags = "Verification",
     hidden = true)
public class VerificationController extends AbstractRestController {

    private VerificationService verificationService;

    private EmailService emailService;

    public VerificationController(final VerificationService verificationService, final EmailService emailService) {
        this.verificationService = verificationService;
        this.emailService = emailService;
    }

    @RequestMapping(value = Paths.EMAIL,
                    method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VerificationDto> createEmailVerification(
            @Valid
            @RequestBody
            final VerificationDto verificationDto) {
        VerificationDto dto = verificationService.generateEmailVerification(verificationDto.getEmail(),
                                                                            verificationDto.getPoolPublicId());
        boolean sent = emailService.sendValidationEmail(dto.getPoolPublicId(), dto.getCode(), dto.getEmail());
        if (!sent) {
            throw new CreditRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, "Email failed to send error.");
        }

        dto.setCode(null);

        return ResponseEntity.ok(dto);
    }

    @ApiOperation(value = "verify")
    @RequestMapping(value = Paths.EMAIL + "/{poolPublicId}/{code}",
                    method = RequestMethod.GET)
    public ResponseEntity<VerificationDto> verifyEmail(
            @PathVariable("poolPublicId")
            final String poolPublicId,
            @PathVariable("code")
            final String code) {

        VerificationDto dto = verificationService.verify(poolPublicId, code);

        return ResponseEntity.ok(dto);
    }

    @RequestMapping(method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VerificationDto> createInfo(
            @RequestBody
            final VerificationDto verificationDto,
            @RequestParam(name = "id",
                          required = true)
            final String id) {
        if (!id.startsWith("zqt")) {
            throw new CreditRuntimeException(HttpStatus.NOT_FOUND);
        }
        VerificationDto dto = verificationService.generateInfoVerification(verificationDto.getInfo()
                                                                                   .trim(),
                                                                           verificationDto.getPoolName()
                                                                                   .trim(),
                                                                           verificationDto.getAllowHtml());

        return ResponseEntity.ok(dto);
    }
}
