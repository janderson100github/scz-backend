package credit.db.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "pool")
public class Pool implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date created;

    @Column(unique = true,
            nullable = false,
            length = 32)
    private String publicId;

    @Column(nullable = false,
            length = 32)
    private String editId;

    @Column(unique = true,
            nullable = false,
            length = 32)
    private String name;

    @Column(nullable = false,
            precision = 19,
            scale = 2)
    private BigDecimal total;

    @Column(name = "des",
            columnDefinition = "TEXT",
            length = 5000)
    private String description;

    @Column
    private Boolean html;

    @Column(length = 128)
    private String email;

    @Column(name = "verified_info",
            length = 128)
    private String verifiedInfo;

    @Column
    private Integer level = 0;

    @Column
    private Boolean verifiedDomainName = Boolean.FALSE;

    @OneToMany(mappedBy = "pool",
               fetch = FetchType.LAZY,
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private List<Item> items = new ArrayList<>();

    public Pool() {
        this.created = new Date();
    }

    public Pool(final String publicId,
                final String editId,
                final String prefix,
                final BigDecimal total,
                final String description,
                final Integer level,
                final String verifiedinfo,
                final Boolean html) {
        this();
        this.publicId = publicId;
        this.editId = editId;
        this.name = prefix;
        this.total = total;
        this.description = description;
        this.level = level;
        this.verifiedInfo = verifiedinfo;
        this.html = html;
    }

    public Long getId() {
        return id;
    }

    public Date getCreated() {
        return created;
    }

    public String getPublicId() {
        return publicId;
    }

    public String getEditId() {
        return editId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public Boolean getVerifiedDomainName() {
        return verifiedDomainName;
    }

    public void setVerifiedDomainName(final Boolean verifiedDomainName) {
        this.verifiedDomainName = verifiedDomainName;
    }

    public String getVerifiedInfo() {
        return verifiedInfo;
    }

    public void setVerifiedInfo(final String verifiedInfo) {
        this.verifiedInfo = verifiedInfo;
    }

    public Boolean getHtml() {
        return html;
    }

    public void setHtml(final Boolean html) {
        this.html = html;
    }

    @Override
    public String toString() {
        return "Pool{" + "id=" + id + ", created=" + created + ", publicId='" + publicId + '\'' + ", editId='" +
               editId + '\'' + ", name='" + name + '\'' + ", total=" + total + ", description='" + description + '\'' +
               '}';
    }
}
