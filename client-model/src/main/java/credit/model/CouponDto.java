package credit.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CouponDto implements Serializable {

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

    @JsonProperty("message")
    private String message;

    @JsonProperty("pool")
    private PoolDto poolDto;

    public CouponDto() {
    }

    public CouponDto(final Long id,
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

    public String getMessage() {
        return message;
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

    @Override
    public String toString() {
        return "ItemDto{" + "id=" + id + ", publicId='" + publicId + '\'' + ", created=" + created + ", amount=" +
               amount + ", level=" + level + ", message='" + message + '\'' + '}';
    }
}
