package credit.core.transformer;

import credit.db.entity.Coupon;
import credit.model.CouponDto;
import org.springframework.stereotype.Component;

@Component
public class CouponDtoTransformer {

    private PoolDtoTransformer poolDtoTransformer;

    public CouponDtoTransformer(final PoolDtoTransformer poolDtoTransformer) {
        this.poolDtoTransformer = poolDtoTransformer;
    }

    public CouponDto generateSimple(final Coupon coupon) {
        if (coupon == null || coupon.getId() == null) {
            return null;
        }
        CouponDto dto = generate(coupon);

        dto.getPoolDto()
                .setEditId(null);
        dto.getPoolDto()
                .setDescription(null);

        return dto;
    }

    public CouponDto generate(final Coupon coupon) {
        if (coupon == null || coupon.getId() == null) {
            return null;
        }

        CouponDto dto = new CouponDto();
        dto.setId(coupon.getId());
        dto.setPublicId(coupon.getPublicId());
        dto.setCreated(coupon.getCreated());
        dto.setAmount(coupon.getAmount());
        dto.setLevel(coupon.getLevel());
        dto.setMessage(coupon.getMessage());
        dto.setPoolDto(poolDtoTransformer.generate(coupon.getPool()));

        return dto;
    }

    public Coupon generate(final CouponDto dto) {
        if (dto.getMessage() != null) {
            dto.setMessage(dto.getMessage()
                                   .trim());
            if ("".equals(dto.getMessage())) {
                dto.setMessage(null);
            }
        }

        return new Coupon(dto.getPublicId(), null, null, dto.getAmount(), dto.getLevel(), dto.getMessage());
    }
}
