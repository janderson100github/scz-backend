package credit.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDeleteRequestDto implements Serializable {

    @JsonProperty("itemPublicId")
    private String itemPublicId;

    @JsonProperty("accountEditId")
    private String accountEditId;

    public ItemDeleteRequestDto() {
    }

    public String getItemPublicId() {
        return itemPublicId;
    }

    public void setItemPublicId(final String itemPublicId) {
        this.itemPublicId = itemPublicId;
    }

    public String getAccountEditId() {
        return accountEditId;
    }

    public void setAccountEditId(final String accountEditId) {
        this.accountEditId = accountEditId;
    }
}
