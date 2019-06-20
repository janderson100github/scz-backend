package credit.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferDto {

    @JsonProperty("fromAccountEditId")
    private String fromAccountEditId;

    @JsonProperty("fromItemPublicId")
    private String fromItemPublicId;

    @JsonProperty("toAccountPublicId")
    private String toAccountPublicId;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("message")
    private String message;

    @JsonProperty("transactionId")
    private String transactionId;

    @JsonProperty("pool")
    private PoolDto poolDto;

    @JsonProperty("toItemPublicId")
    private String toItemPublicId;

    public TransferDto() {
    }

    public TransferDto(final String fromAccountEditId,
                       final String fromItemPublicId,
                       final String toAccountPublicId,
                       final BigDecimal amount,
                       final String message) {
        this.fromAccountEditId = fromAccountEditId;
        this.fromItemPublicId = fromItemPublicId;
        this.toAccountPublicId = toAccountPublicId;
        this.amount = amount;
        this.message = message;
    }

    public String getFromAccountEditId() {
        return fromAccountEditId;
    }

    public void setFromAccountEditId(final String fromAccountEditId) {
        this.fromAccountEditId = fromAccountEditId;
    }

    public String getFromItemPublicId() {
        return fromItemPublicId;
    }

    public void setFromItemPublicId(final String fromItemPublicId) {
        this.fromItemPublicId = fromItemPublicId;
    }

    public String getToAccountPublicId() {
        return toAccountPublicId;
    }

    public void setToAccountPublicId(final String toAccountPublicId) {
        this.toAccountPublicId = toAccountPublicId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(final String transactionId) {
        this.transactionId = transactionId;
    }

    public PoolDto getPoolDto() {
        return poolDto;
    }

    public void setPoolDto(final PoolDto poolDto) {
        this.poolDto = poolDto;
    }

    public String getToItemPublicId() {
        return toItemPublicId;
    }

    public void setToItemPublicId(final String toItemPublicId) {
        this.toItemPublicId = toItemPublicId;
    }
}
