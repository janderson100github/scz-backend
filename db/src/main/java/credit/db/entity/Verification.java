package credit.db.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "verification")
public class Verification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date created = new Date();

    @Column(unique = true,
            nullable = false,
            length = 32)
    private String code;

    @Column
    Boolean verified = Boolean.FALSE;

    @Column(length = 128)
    private String email;

    @Column(length = 128)
    private String domain;

    @Column(length = 128)
    private String info;

    @Column(name = "pool_public_id",
            length = 32)
    private String poolPublicId;

    @Column(name = "allow_html")
    private Boolean allowHtml;

    public Verification() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(final Date created) {
        this.created = created;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(final Boolean verified) {
        this.verified = verified;
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

    public String getPoolPublicId() {
        return poolPublicId;
    }

    public void setPoolPublicId(final String poolPublicId) {
        this.poolPublicId = poolPublicId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(final String info) {
        this.info = info;
    }

    public Boolean getAllowHtml() {
        return allowHtml;
    }

    public void setAllowHtml(final Boolean allowHtml) {
        this.allowHtml = allowHtml;
    }
}
