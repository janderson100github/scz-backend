package credit;

import credit.model.AccountDto;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;

@Ignore
public class AccountControllerIntegrationTest {

    private static final String HOST = "http://localhost:8080";

    private static final String HOST_ACCOUNT = HOST + Paths.ACCOUNT;

    private RestTemplate restTemplate = new RestTemplate();

    @Before
    public void beforeMethod() {
    }

    @Test
    public void GET_notFound() {
        String url = HOST_ACCOUNT + "/publicId";
        AccountDto accountDto = restTemplate.exchange(url, HttpMethod.GET, generateRequestEntity(), AccountDto.class)
                .getBody();
        assertNull(accountDto);
    }

    @Test
    public void GET_found() {
        String publicId = "oNuwEQLIwdS0RhS8";
        String url = HOST_ACCOUNT + "/" + publicId;
        AccountDto accountDto = restTemplate.exchange(url, HttpMethod.GET, generateRequestEntity(), AccountDto.class)
                .getBody();
        assertNotNull(accountDto);
        assertNotNull(accountDto.getItems()
                              .get(0)
                              .getTransactionId());
        System.out.println(accountDto.toString());
    }

    @Test
    public void POST_PUT_DELETE() {
        AccountDto reqDto = new AccountDto();
        reqDto.setDescription("description");

        // -- POST --
        AccountDto accountDto = restTemplate.exchange(HOST_ACCOUNT, HttpMethod.POST, generateRequestEntity(reqDto),
                                                      AccountDto.class)
                .getBody();
        assertNotNull(accountDto);
        assertEquals(reqDto.getDescription(), accountDto.getDescription());
        assertNotNull(accountDto.getId());

        String editId = accountDto.getEditId();

        // -- GET public --
        accountDto = restTemplate.exchange(HOST_ACCOUNT + "/" + accountDto.getPublicId(), HttpMethod.GET,
                                           generateRequestEntity(), AccountDto.class)
                .getBody();
        assertNotNull(accountDto);
        assertNull(accountDto.getEditId());

        // -- GET private --
        accountDto = restTemplate.exchange(HOST_ACCOUNT + "/" + accountDto.getPublicId() + "?editId=" + editId,
                                           HttpMethod.GET, generateRequestEntity(), AccountDto.class)
                .getBody();
        assertNotNull(accountDto);
        assertEquals(editId, accountDto.getEditId());

        // -- PUT - FAIL --
        reqDto.setId(accountDto.getId());
        reqDto.setDescription("new description");
        try {
            final ResponseEntity<AccountDto> response = restTemplate.exchange(HOST_ACCOUNT, HttpMethod.PUT,
                                                                              generateRequestEntity(reqDto),
                                                                              AccountDto.class);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }

        // -- PUT - SUCCEED --
        reqDto.setId(accountDto.getId());
        reqDto.setPublicId(accountDto.getPublicId());
        reqDto.setEditId(editId);
        reqDto.setDescription("new description");
        accountDto = restTemplate.exchange(HOST_ACCOUNT, HttpMethod.PUT, generateRequestEntity(reqDto),
                                           AccountDto.class)
                .getBody();
        assertEquals(reqDto.getId(), accountDto.getId());
        assertEquals(reqDto.getDescription(), accountDto.getDescription());

        // -- DELETE --
        final ResponseEntity<Void> response = restTemplate.exchange(HOST_ACCOUNT, HttpMethod.DELETE,
                                                                    generateRequestEntity(reqDto), Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        // -- GET --
        try {
            accountDto = restTemplate.exchange(HOST_ACCOUNT + "/" + reqDto.getPublicId(), HttpMethod.GET,
                                               generateRequestEntity(), AccountDto.class)
                    .getBody();
        } catch (HttpClientErrorException e) {
            assertEquals(404, e.getStatusCode()
                    .value());
        }
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
