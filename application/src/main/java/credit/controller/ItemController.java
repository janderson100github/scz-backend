package credit.controller;

import credit.Paths;
import credit.core.service.ItemService;
import credit.model.ItemDeleteRequestDto;
import credit.model.ItemDto;
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

import javax.validation.Valid;

@RestController
@RequestMapping(value = Paths.API + Paths.ITEM,
                produces = MediaType.APPLICATION_JSON_VALUE)
@Api(description = "Item endpoints",
     tags = "Item")
public class ItemController {

    private ItemService itemService;

    public ItemController(final ItemService itemService) {
        this.itemService = itemService;
    }

    @ApiOperation(value = "getItem")
    @RequestMapping(value = "/{publicId}",
                    method = RequestMethod.GET)
    public ResponseEntity<ItemDto> getAccount(
            @PathVariable("publicId")
            final String publicId) {

        ItemDto itemDto = itemService.getItem(publicId);

        if (itemDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(itemDto);
    }

    @RequestMapping(method = RequestMethod.DELETE,
                    consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteItem(
            @Valid
            @RequestBody
            final ItemDeleteRequestDto itemDeleteRequestDto) {
        itemService.deleteItem(itemDeleteRequestDto.getAccountEditId(), itemDeleteRequestDto.getItemPublicId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
