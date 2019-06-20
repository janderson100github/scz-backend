package credit.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto implements Serializable {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("publicId")
    private String publicId;

    @JsonProperty("editId")
    private String editId;

    @JsonProperty("verificationId")
    private String verificationId;

    @JsonProperty("created")
    private Date created;

    @JsonProperty("description")
    private String description;

    @JsonProperty("visibility")
    private String visibility;

    @JsonProperty("alias")
    private String alias;

    @JsonProperty("score")
    private Integer score;

    @JsonProperty("html")
    private Boolean html;

    @JsonProperty("verifiedInfo")
    private String verifiedInfo;

    @JsonProperty("items")
    private List<ItemDto> items = new ArrayList<>();

    public AccountDto() {
    }

    public AccountDto(final Long id,
                      final String publicId,
                      final String editId,
                      final Date created,
                      final String description,
                      final String visibility,
                      final Integer score) {
        this.id = id;
        this.publicId = publicId;
        this.editId = editId;
        this.created = created;
        this.description = description;
        this.visibility = visibility;
        this.score = score;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(final String visibility) {
        this.visibility = visibility;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(final Integer score) {
        this.score = score;
    }

    public List<ItemDto> getItems() {
        return items;
    }

    public void setItems(final List<ItemDto> items) {
        this.items = items;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(final String alias) {
        this.alias = alias;
    }

    public String getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(final String verificationId) {
        this.verificationId = verificationId;
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
        return "AccountDto{" + "id=" + id + ", publicId='" + publicId + '\'' + ", editId='" + editId + '\'' +
               ", description='" + description + '\'' + ", visibility='" + visibility + '\'' + ", score=" + score +
               ", items=" + items + '}';
    }
}
