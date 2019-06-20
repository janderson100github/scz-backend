package credit.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import credit.Paths;
import credit.core.exception.CreditRuntimeException;
import credit.core.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

@RestController
@RequestMapping(value = Paths.API + Paths.PROOF,
                produces = MediaType.APPLICATION_JSON_VALUE)
@Api(description = "Proof endpoints",
     tags = "Proof")
public class ProofController extends AbstractRestController {

    private AccountService accountService;

    public ProofController(AccountService accountService) {
        this.accountService = accountService;
    }

    @ApiOperation(value = "getAccount",
                  hidden = true)
    @RequestMapping(value = "/{editId}",
                    method = RequestMethod.GET)
    public ResponseEntity<ProofDto> getAccount(
            @PathVariable("editId")
                    String editId) {

        if (!editId.startsWith("9951") || editId.length() < 8) {
            throw new CreditRuntimeException(HttpStatus.BAD_REQUEST);
        }
        editId = editId.replaceFirst("9951", "");

        List<String> lines = accountService.getRecentAccounts(editId);

        return ResponseEntity.ok(new ProofDto(lines));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private class ProofDto implements Serializable {

        @JsonProperty("lines")
        public List<String> lines;

        public ProofDto(final List<String> lines) {
            this.lines = lines;
        }
    }
}
