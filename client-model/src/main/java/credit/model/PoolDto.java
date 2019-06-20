package credit.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PoolDto implements Serializable {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("publicId")
    private String publicId;

    @JsonProperty("editId")
    private String editId;

    @JsonProperty("created")
    private Date created;

    @JsonProperty("name")
    private String name;

    @JsonProperty("total")
    private BigDecimal total;

    @JsonProperty("description")
    private String description;

    @JsonProperty("html")
    private Boolean html;

    @JsonProperty("verifiedInfo")
    private String verifiedInfo;

    @JsonProperty("verificationCode")
    private String verificationCode;

    @JsonProperty("level")
    private Integer level;

    @JsonProperty("domainVerificationCode")
    private String domainVerificationCode;

    public PoolDto() {
    }

    public PoolDto(final Long id,
                   final String publicId,
                   final String editId,
                   final Date created,
                   final String name,
                   final BigDecimal total,
                   final String description) {
        this.id = id;
        this.publicId = publicId;
        this.editId = editId;
        this.created = created;
        this.name = name;
        this.total = total;
        this.description = description;
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

    public String getEditId() {
        return editId;
    }

    public void setEditId(final String editId) {
        this.editId = editId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(final Date created) {
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(final BigDecimal total) {
        this.total = total;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(final Integer level) {
        this.level = level;
    }

    public String getDomainVerificationCode() {
        return domainVerificationCode;
    }

    public void setDomainVerificationCode(final String domainVerificationCode) {
        this.domainVerificationCode = domainVerificationCode;
    }

    public String getVerifiedInfo() {
        return verifiedInfo;
    }

    public void setVerifiedInfo(final String verifiedInfo) {
        this.verifiedInfo = verifiedInfo;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(final String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Boolean getHtml() {
        return html;
    }

    public void setHtml(final Boolean html) {
        this.html = html;
    }
}
