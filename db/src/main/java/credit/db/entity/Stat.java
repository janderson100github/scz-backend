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
@Table(name = "stat")
public class Stat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date created = new Date();

    @Column(nullable = false,
            length = 32)
    private String ip;

    @Column(length = 32)
    private String item;

    @Column(length = 32)
    private String info;

    public Stat(final String ip, final String item, final String info) {
        this.ip = ip;
        this.item = item;
        this.info = info;
    }

    public Long getId() {
        return id;
    }

    public Date getCreated() {
        return created;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(final String ip) {
        this.ip = ip;
    }

    public String getItem() {
        return item;
    }

    public void setItem(final String item) {
        this.item = item;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(final String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "Stat{" + "id=" + id + ", ip='" + ip + '\'' + ", item='" + item + '\'' + ", info='" + info + '\'' + '}';
    }
}
