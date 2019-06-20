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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "account")
public class Account implements Serializable {

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

    @Column(name = "des",
            columnDefinition = "TEXT",
            length = 5000)
    private String description;

    @Column(length = 32)
    private String visibility = "public";

    @Column(length = 128)
    private String alias;

    @Column
    private Integer score = 0;

    @Column
    private Boolean html;

    @Column(name = "verified_info",
            length = 128)
    private String verifiedInfo;

    @OneToMany(mappedBy = "account",
               fetch = FetchType.EAGER,
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private List<Item> items = new ArrayList<>();

    public Account() {
        this.created = new Date();
    }

    public Account(final String publicId,
                   final String editId,
                   final String description,
                   final String alias,
                   final Boolean html,
                   final List<Item> items) {
        this();
        this.publicId = publicId;
        this.editId = editId;
        this.description = description;
        this.items = items;
        this.alias = alias;
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

    public String getDescription() {
        return description;
    }

    public List<Item> getItems() {
        return items;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setPublicId(final String publicId) {
        this.publicId = publicId;
    }

    public void setEditId(final String editId) {
        this.editId = editId;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setVisibility(final String visibility) {
        this.visibility = visibility;
    }

    public void setItems(final List<Item> items) {
        this.items = items;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(final Integer score) {
        this.score = score;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(final String alias) {
        this.alias = alias;
    }

    public Boolean getHtml() {
        return html;
    }

    public void setHtml(final Boolean html) {
        this.html = html;
    }

    public String getVerifiedInfo() {
        return verifiedInfo;
    }

    public void setVerifiedInfo(final String verifiedInfo) {
        this.verifiedInfo = verifiedInfo;
    }

    @Override
    public String toString() {
        return "Account{" + "id=" + id + ", publicId='" + publicId + '\'' + ", editId='" + editId + '\'' + '}';
    }
}
