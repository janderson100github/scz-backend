package credit.controller;

import credit.Paths;
import credit.core.exception.CreditRuntimeException;
import credit.core.service.AccountService;
import credit.core.service.ItemService;
import credit.core.service.PoolService;
import credit.core.service.StatService;
import credit.core.transformer.ItemDtoTransformer;
import credit.model.AccountDto;
import credit.model.ItemDto;
import credit.model.PoolAccountDto;
import credit.model.PoolDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = Paths.API + Paths.POOL,
                produces = MediaType.APPLICATION_JSON_VALUE)
@Api(description = "Pool endpoints",
     tags = "Pool")
public class PoolController extends AbstractRestController {

    @Value(value = "${pool.pre.prefix:SC_}")
    private String poolPrePrefix;

    private PoolService poolService;

    private AccountService accountService;

    private ItemService itemService;

    private ItemDtoTransformer itemDtoTransformer;

    private StatService statService;

    public PoolController(PoolService poolService,
                          final AccountService accountService,
                          final ItemService itemService,
                          final ItemDtoTransformer itemDtoTransformer,
                          final StatService statService) {
        this.poolService = poolService;
        this.accountService = accountService;
        this.itemService = itemService;
        this.itemDtoTransformer = itemDtoTransformer;
        this.statService = statService;
    }

    @ApiOperation(value = "getPool")
    @RequestMapping(value = "/{name}",
                    method = RequestMethod.GET)
    public ResponseEntity<PoolDto> getPool(
            @PathVariable("name")
            final String name, HttpServletRequest request) {

        if ("ipo".equalsIgnoreCase(name)) {
            statService.insertStat(request.getRemoteAddr(), "ipo", null);
        }

        PoolDto poolDto = poolService.getPool(name);

        if (poolDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        poolDto.setEditId(null);

        return ResponseEntity.ok(poolDto);
    }

    @RequestMapping(method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PoolAccountDto> createPool(
            @Valid
            @RequestBody
            final PoolDto poolDto, HttpServletRequest request) {
        validatePoolDto(poolDto);

        PoolDto savedPoolDto = poolService.createPool(poolDto);
        AccountDto accountDto = accountService.createAccount(request.getRemoteAddr());
        ItemDto itemDto = itemDtoTransformer.generate(
                itemService.addNewItemToAccount(savedPoolDto.getName(), accountDto.getPublicId(),
                                                savedPoolDto.getTotal()));
        accountDto.getItems()
                .add(itemDto);

        statService.insertStat(request.getRemoteAddr(), "createPool", poolDto.getName()
                .substring(0, Math.min(32, poolDto.getName()
                        .length())));

        PoolAccountDto poolAccountDto = new PoolAccountDto(savedPoolDto, accountDto);

        return ResponseEntity.ok(poolAccountDto);
    }

    @RequestMapping(method = RequestMethod.PUT,
                    consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PoolDto> updatePool(
            @Valid
            @RequestBody
            final PoolDto poolDto) {
        PoolDto savedPoolDto = poolService.update(poolDto);
        return ResponseEntity.ok(savedPoolDto);
    }

    @RequestMapping(method = RequestMethod.DELETE,
                    consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deletePool(
            @Valid
            @RequestBody
            final PoolDto poolDto) {
        poolService.delete(poolDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validatePoolDto(final PoolDto poolDto) {
        poolDto.setName(poolDto.getName()
                                .trim());
        if (StringUtils.containsWhitespace(poolDto.getName())) {
            throw new CreditRuntimeException(HttpStatus.PRECONDITION_FAILED, "Pool name cannot contain spaces");
        }
    }
}
