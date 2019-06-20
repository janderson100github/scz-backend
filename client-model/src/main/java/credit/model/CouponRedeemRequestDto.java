package credit.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CouponRedeemRequestDto implements Serializable {

    @JsonProperty("couponPublicId")
    private String couponPublicId;

    @JsonProperty("accountEditId")
    private String accountEditId;

    public CouponRedeemRequestDto() {
    }

    public String getCouponPublicId() {
        return couponPublicId;
    }

    public void setCouponPublicId(final String couponPublicId) {
        this.couponPublicId = couponPublicId;
    }

    public String getAccountEditId() {
        return accountEditId;
    }

    public void setAccountEditId(final String accountEditId) {
        this.accountEditId = accountEditId;
    }
}
