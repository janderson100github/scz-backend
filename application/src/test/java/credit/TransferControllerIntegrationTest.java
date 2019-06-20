package credit;

import credit.model.AccountDto;
import credit.model.PoolAccountDto;
import credit.model.PoolDto;
import credit.model.TransferDto;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TransferControllerIntegrationTest {

    private static final String HOST = "http://localhost:8080";

    private static final String HOST_ACCOUNT = HOST + Paths.ACCOUNT;

    private static final String HOST_POOL = HOST + Paths.POOL;

    private static final String HOST_TRANSFER = HOST + Paths.TRANSFER;

    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void POST() {
        // -- new pool --
        PoolDto reqDto = new PoolDto();
        reqDto.setName(RandomStringUtils.randomAlphanumeric(32));
        reqDto.setDescription("description");
        PoolAccountDto poolAccountDto = restTemplate.exchange(HOST_POOL, HttpMethod.POST, generateRequestEntity(reqDto),
                                                              PoolAccountDto.class)
                .getBody();
        PoolDto poolDto = poolAccountDto.getPoolDto();
        AccountDto accountFromDto = poolAccountDto.getAccount();

        // -- new account --
        AccountDto accountReqDto = new AccountDto();
        reqDto.setDescription("description");
        AccountDto accountToDto = restTemplate.exchange(HOST_ACCOUNT, HttpMethod.POST,
                                                        generateRequestEntity(accountReqDto), AccountDto.class)
                .getBody();

        // -- transfer --
        TransferDto transferDto = new TransferDto(accountFromDto.getEditId(), accountFromDto.getItems()
                .get(0)
                .getPublicId(), accountToDto.getPublicId(), new BigDecimal(99.99).setScale(2, BigDecimal.ROUND_HALF_UP),
                                                  "message");
        transferDto = restTemplate.exchange(HOST_TRANSFER, HttpMethod.POST, generateRequestEntity(transferDto),
                                            TransferDto.class)
                .getBody();
        assertNotNull(transferDto.getTransactionId());

        // -- get to account --
        accountToDto = restTemplate.exchange(HOST_ACCOUNT + "/" + accountToDto.getPublicId(), HttpMethod.GET,
                                             generateRequestEntity(), AccountDto.class)
                .getBody();
        assertEquals(transferDto.getAmount(), accountToDto.getItems()
                .get(0)
                .getAmount());

        // -- get from account --
        accountFromDto = restTemplate.exchange(HOST_ACCOUNT + "/" + accountFromDto.getPublicId(), HttpMethod.GET,
                                               generateRequestEntity(), AccountDto.class)
                .getBody();
        assertEquals(poolDto.getTotal()
                             .subtract(transferDto.getAmount()), accountFromDto.getItems()
                             .get(0)
                             .getAmount());
    }

    private HttpEntity<?> generateRequestEntity(TransferDto req) {
        return new HttpEntity<>(req, generateHeaders());
    }

    private HttpEntity<?> generateRequestEntity(PoolDto req) {
        return new HttpEntity<>(req, generateHeaders());
    }

    private HttpEntity<?> generateRequestEntity(AccountDto req) {
        return new HttpEntity<>(req, generateHeaders());
    }

    private HttpEntity<?> generateRequestEntity() {
        return new HttpEntity<>(generateHeaders());
    }

    private HttpHeaders generateHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
