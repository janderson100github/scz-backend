package credit.controller;

import credit.Paths;
import credit.core.exception.CreditRuntimeException;
import credit.core.service.AccountService;
import credit.core.utl.TokenGenerator;
import credit.model.AccountDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = Paths.API + Paths.ACCOUNT,
                produces = MediaType.APPLICATION_JSON_VALUE)
@Api(description = "Account endpoints",
     tags = "Account")
public class AccountController extends AbstractRestController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @ApiOperation(value = "getAccount")
    @RequestMapping(value = "/{publicId}",
                    method = RequestMethod.GET)
    public ResponseEntity<AccountDto> getAccount(
            @PathVariable("publicId")
            final String publicId,
            @RequestParam(name = "editId",
                          required = false)
            final String editId) {

        AccountDto accountDto = accountService.getAccount(TokenGenerator.stringToId(publicId),
                                                          TokenGenerator.stringToId(editId));

        if (accountDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(accountDto);
    }

    @ApiOperation(value = "getAccountByAlias")
    @RequestMapping(value = Paths.ALIAS + "/{alias}",
                    method = RequestMethod.GET)
    public ResponseEntity<AccountDto> getAccountByAlias(
            @PathVariable("alias")
            final String alias) {

        AccountDto accountDto = accountService.getAccountByAlias(alias);

        if (accountDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(accountDto);
    }

    @RequestMapping(method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> createAccount(
            @Valid
            @RequestBody
            final AccountDto accountDto, HttpServletRequest request) {
        validateAccountDto(accountDto);
        AccountDto savedAccountDto = accountService.createAccount(accountDto, request.getRemoteAddr());
        return ResponseEntity.ok(savedAccountDto);
    }

    @RequestMapping(method = RequestMethod.PUT,
                    consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> updateAccount(
            @Valid
            @RequestBody
            final AccountDto accountDto) {
        AccountDto savedAccountDto = accountService.update(accountDto);
        return ResponseEntity.ok(savedAccountDto);
    }

    @RequestMapping(method = RequestMethod.DELETE,
                    consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteAccount(
            @Valid
            @RequestBody
            final AccountDto accountDto) {
        accountService.delete(accountDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateAccountDto(final AccountDto accountDto) {
        if (accountDto.getAlias() == null) {
            return;
        }

        accountDto.setAlias(accountDto.getAlias()
                                    .trim());
        if (StringUtils.containsWhitespace(accountDto.getAlias())) {
            throw new CreditRuntimeException(HttpStatus.PRECONDITION_FAILED, "Alias cannot contain spaces");
        }
    }
}
