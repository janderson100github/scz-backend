package credit.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CouponGenerateResponseDto implements Serializable {

    @JsonProperty("coupons")
    private List<CouponDto> coupons;

    public CouponGenerateResponseDto() {
    }

    public CouponGenerateResponseDto(final List<CouponDto> coupons) {
        this.coupons = coupons;
    }

    public List<CouponDto> getCoupons() {
        return coupons;
    }

    public void setCoupons(final List<CouponDto> coupons) {
        this.coupons = coupons;
    }
}
