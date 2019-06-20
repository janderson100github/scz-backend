package credit.core.transformer;

import credit.db.entity.Item;
import credit.model.ItemDto;
import org.springframework.stereotype.Component;

@Component
public class ItemDtoTransformer {

    private PoolDtoTransformer poolDtoTransformer;

    public ItemDtoTransformer(final PoolDtoTransformer poolDtoTransformer) {
        this.poolDtoTransformer = poolDtoTransformer;
    }

    public ItemDto generate(final Item item) {
        if (item == null || item.getId() == null) {
            return null;
        }

        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setPublicId(item.getPublicId());
        dto.setCreated(item.getCreated());
        dto.setAmount(item.getAmount());
        dto.setLevel(item.getLevel());
        dto.setTransactionId(item.getTransactionId());
        dto.setMessage(item.getMessage());
        dto.setPoolDto(poolDtoTransformer.generate(item.getPool()));

        return dto;
    }

    public Item generate(final ItemDto dto) {
        return new Item(dto.getPublicId(), null, null, dto.getAmount(), dto.getLevel(), null, dto.getMessage());
    }
}
