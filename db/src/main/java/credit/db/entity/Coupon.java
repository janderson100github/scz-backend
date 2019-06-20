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
@Table(name = "coupon")
public class Coupon implements Serializable {

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

    @Column(length = 32)
    private String accountPublicId;

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

    @Column
    private String message;

    public Coupon() {
        this.created = new Date();
    }

    public Coupon(final String publicId,
                  final String accountPublicId,
                  final Pool pool,
                  final BigDecimal amount,
                  final Integer level,
                  final String message) {
        this();
        this.publicId = publicId;
        this.accountPublicId = accountPublicId;
        this.pool = pool;
        this.amount = amount;
        this.level = level;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public Date getCreated() {
        return created;
    }

    public Pool getPool() {
        return pool;
    }

    public void setPool(final Pool pool) {
        this.pool = pool;
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

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(final String publicId) {
        this.publicId = publicId;
    }

    public String getAccountPublicId() {
        return accountPublicId;
    }

    public void setAccountPublicId(final String accountPublicId) {
        this.accountPublicId = accountPublicId;
    }

    @Override
    public String toString() {
        return "Coupon{" + "id=" + id + ", pool=" + pool.getName() + ", amount=" + amount + ", level=" + level +
               ", message='" + message + '\'' + '}';
    }
}
