package credit.db.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "item")
public class Item implements Serializable {

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

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.EAGER,
               cascade = CascadeType.ALL)
    @JoinColumn(name = "pool_id")
    private Pool pool;

    @Column(nullable = false,
            precision = 19,
            scale = 2)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column
    private Integer level = 1;

    @Column(unique = true,
            nullable = false,
            length = 32)
    private String transactionId;

    @Column(length = 255)
    private String message;

    public Item() {
        this.created = new Date();
    }

    public Item(final String publicId,
                final Pool pool,
                final Account account,
                final BigDecimal amount,
                final Integer level,
                final String transactionId,
                final String message) {
        this();
        this.publicId = publicId;
        this.pool = pool;
        this.account = account;
        this.amount = amount;
        this.level = level;
        this.transactionId = transactionId;
        this.message = message;
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

    public Account getAccount() {
        return account;
    }

    public Pool getPool() {
        return pool;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Integer getLevel() {
        return level;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getMessage() {
        return message;
    }

    public void setPublicId(final String publicId) {
        this.publicId = publicId;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public void setTransactionId(final String transactionId) {
        this.transactionId = transactionId;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public void setAccount(final Account account) {
        this.account = account;
    }

    public void setPool(final Pool pool) {
        this.pool = pool;
    }

    public void setLevel(final Integer level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", created=" + created + ", publicId='" + publicId + '\'' + ", account=" +
               account + ", pool=" + pool.getName() + ", amount=" + amount + ", level=" + level + '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Item item = (Item) o;

        return id.equals(item.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
