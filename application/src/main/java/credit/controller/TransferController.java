package credit.controller;

import credit.Paths;
import credit.core.exception.CreditRuntimeException;
import credit.core.service.ItemService;
import credit.core.service.TransferService;
import credit.model.ItemDto;
import credit.model.TransferDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping(value = Paths.API + Paths.TRANSFER,
                produces = MediaType.APPLICATION_JSON_VALUE)
@Api(description = "Transfer endpoints",
     tags = "Transfer")
public class TransferController {

    private TransferService transferService;

    private ItemService itemService;

    public TransferController(final TransferService transferService, final ItemService itemService) {
        this.transferService = transferService;
        this.itemService = itemService;
    }

    @RequestMapping(method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransferDto> transfer(
            @RequestBody
            final TransferDto transferDto) {
        if (transferDto.getAmount() == null || transferDto.getAmount()
                                                       .compareTo(BigDecimal.ZERO) <= 0) {
            throw new CreditRuntimeException(HttpStatus.BAD_REQUEST, "Amount invalid " + transferDto.getAmount());
        }
        transferDto.getAmount()
                .setScale(2, RoundingMode.FLOOR);
        TransferDto updatedTransferDto = transferService.transfer(transferDto.getFromAccountEditId(),
                                                                  transferDto.getFromItemPublicId(),
                                                                  transferDto.getToAccountPublicId(),
                                                                  transferDto.getAmount(), transferDto.getMessage());

        return ResponseEntity.ok(updatedTransferDto);
    }

    @ApiOperation(value = "getTransfer")
    @RequestMapping(value = "/{transactionId}",
                    method = RequestMethod.GET)
    public ResponseEntity<TransferDto> getTransfer(
            @PathVariable("transactionId")
            final String transactionId) {
        ItemDto itemDto = itemService.getItemByTransactionId(transactionId);

        return ResponseEntity.ok(generateTransferDto(itemDto));
    }

    private TransferDto generateTransferDto(final ItemDto itemDto) {
        TransferDto transferDto = new TransferDto();
        transferDto.setTransactionId(itemDto.getTransactionId());
        transferDto.setAmount(itemDto.getAmount());
        transferDto.setMessage(itemDto.getMessage());
        transferDto.setPoolDto(itemDto.getPoolDto());
        return transferDto;
    }
}
