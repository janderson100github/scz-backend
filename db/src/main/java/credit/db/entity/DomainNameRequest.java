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
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "domain_prefix_request")
public class DomainNameRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date created;

    @Column(unique = true,
            nullable = false,
            length = 128)
    private String prefix;

    @Column(length = 32)
    private String code;

    @Column(length = 32)
    private String poolEditId;

    public DomainNameRequest() {
    }

    public DomainNameRequest(final String prefix, final String code, final String poolEditId) {
        this.prefix = prefix;
        this.code = code;
        this.poolEditId = poolEditId;
    }

    public Long getId() {
        return id;
    }

    public Date getCreated() {
        return created;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getCode() {
        return code;
    }

    public String getPoolEditId() {
        return poolEditId;
    }
}
