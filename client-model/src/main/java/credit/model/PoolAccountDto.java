package credit.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PoolAccountDto implements Serializable {

    @JsonProperty("pool")
    private PoolDto poolDto;

    @JsonProperty("account")
    private AccountDto account;

    public PoolAccountDto() {
    }

    public PoolAccountDto(final PoolDto poolDto, final AccountDto account) {
        this.poolDto = poolDto;
        this.account = account;
    }

    public PoolDto getPoolDto() {
        return poolDto;
    }

    public void setPoolDto(final PoolDto poolDto) {
        this.poolDto = poolDto;
    }

    public AccountDto getAccount() {
        return account;
    }

    public void setAccount(final AccountDto account) {
        this.account = account;
    }
}
