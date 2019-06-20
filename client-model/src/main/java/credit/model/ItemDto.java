package credit.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDto implements Serializable, Comparable<ItemDto> {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("publicId")
    private String publicId;

    @JsonProperty("created")
    private Date created;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("level")
    private Integer level;

    @JsonProperty("transactionId")
    private String transactionId;

    @JsonProperty("message")
    private String message;

    @JsonProperty("pool")
    private PoolDto poolDto;

    @JsonProperty("account")
    private AccountDto accountDto;

    public ItemDto() {
    }

    public ItemDto(final Long id,
                   final String publicId,
                   final Date created,
                   final BigDecimal amount,
                   final Integer level,
                   final PoolDto poolDto) {
        this.id = id;
        this.publicId = publicId;
        this.created = created;
        this.amount = amount;
        this.level = level;
        this.poolDto = poolDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(final String publicId) {
        this.publicId = publicId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(final Date created) {
        this.created = created;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(final Integer level) {
        this.level = level;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getMessage() {
        return message;
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

    public void setMessage(final String message) {
        this.message = message;
    }

    public AccountDto getAccountDto() {
        return accountDto;
    }

    public void setAccountDto(final AccountDto accountDto) {
        this.accountDto = accountDto;
    }

    @Override
    public String toString() {
        return "ItemDto{" + "id=" + id + ", publicId='" + publicId + '\'' + ", created=" + created + ", amount=" +
               amount + ", level=" + level + ", transactionId='" + transactionId + '\'' + ", message='" + message +
               '\'' + '}';
    }

    @Override
    public int compareTo(final ItemDto other) {
        return this.toComparableString(this)
                .compareTo(toComparableString(other));
    }

    private String toComparableString(ItemDto dto) {
        return dto.getPoolDto()
                       .getName() + dto.getLevel();
    }
}
