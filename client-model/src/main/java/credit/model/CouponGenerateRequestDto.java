package credit.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CouponGenerateRequestDto implements Serializable {

    @JsonProperty("itemPublicId")
    private String itemPublicId;

    @JsonProperty("accountEditId")
    private String accountEditId;

    @JsonProperty("message")
    private String message;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("quantity")
    private Integer quantity = 1;

    public CouponGenerateRequestDto() {
    }

    public String getAccountEditId() {
        return accountEditId;
    }

    public void setAccountEditId(final String accountEditId) {
        this.accountEditId = accountEditId;
    }

    public String getItemPublicId() {
        return itemPublicId;
    }

    public void setItemPublicId(final String itemPublicId) {
        this.itemPublicId = itemPublicId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(final Integer quantity) {
        this.quantity = quantity;
    }
}
