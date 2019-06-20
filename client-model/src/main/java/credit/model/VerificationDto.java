package credit.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerificationDto {

    @JsonProperty("poolPublicId")
    private String poolPublicId;

    @JsonProperty("poolName")
    private String poolName;

    @JsonProperty("code")
    private String code;

    @JsonProperty("email")
    private String email;

    @JsonProperty("domain")
    private String domain;

    @JsonProperty("info")
    private String info;

    @JsonProperty("allowHtml")
    private Boolean allowHtml;

    public VerificationDto() {
    }

    public VerificationDto(final String poolPublicId, final String email) {
        this.poolPublicId = poolPublicId;
        this.email = email;
    }

    public VerificationDto(final String poolPublicId,
                           final String code,
                           final String email,
                           final String domain,
                           final String info) {
        this.poolPublicId = poolPublicId;
        this.code = code;
        this.email = email;
        this.domain = domain;
        this.info = info;
    }

    public String getPoolPublicId() {
        return poolPublicId;
    }

    public void setPoolPublicId(final String poolPublicId) {
        this.poolPublicId = poolPublicId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(final String domain) {
        this.domain = domain;
    }

    public void setInfo(final String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(final String poolName) {
        this.poolName = poolName;
    }

    public Boolean getAllowHtml() {
        return allowHtml;
    }

    public void setAllowHtml(final Boolean allowHtml) {
        this.allowHtml = allowHtml;
    }
}
