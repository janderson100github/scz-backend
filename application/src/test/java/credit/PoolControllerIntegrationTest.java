package credit;

import credit.model.AccountDto;
import credit.model.PoolAccountDto;
import credit.model.PoolDto;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
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

public class PoolControllerIntegrationTest {

    private static final String HOST = "http://localhost:8090";

    private static final String HOST_POOL = HOST + Paths.POOL;

    private RestTemplate restTemplate = new RestTemplate();

    @Before
    public void beforeMethod() {
    }

    @Test
    public void GET_notFound() {
        String url = HOST_POOL + "/no";
        try {
            PoolDto poolDto = restTemplate.exchange(url, HttpMethod.GET, generateRequestEntity(), PoolDto.class)
                    .getBody();
        } catch (HttpClientErrorException e) {
            assertEquals(404, e.getStatusCode()
                    .value());
        }
    }

    @Test
    public void POST() {
        String url = HOST_POOL;

        PoolDto reqDto = new PoolDto();
        reqDto.setName(RandomStringUtils.randomAlphanumeric(32));
        reqDto.setDescription("description");

        // -- POST --
        PoolAccountDto poolAccountDto = null;
        try {
            poolAccountDto = restTemplate.exchange(url, HttpMethod.POST, generateRequestEntity(reqDto),
                                                   PoolAccountDto.class)
                    .getBody();
        } catch (HttpClientErrorException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        PoolDto poolDto = poolAccountDto.getPoolDto();
        assertNotNull(poolDto);
        assertEquals(reqDto.getName(), poolDto.getName());
        assertNotNull(poolDto.getId());

        AccountDto accountDto = poolAccountDto.getAccount();
        assertNotNull(accountDto.getId());

        assertEquals(1, accountDto.getItems()
                .size());
    }

    @Test
    public void POST_PUT_DELETE() {
        String url = HOST_POOL;

        PoolDto reqDto = new PoolDto();
        reqDto.setName("prefix");
        reqDto.setDescription("description");

        // -- POST --
        PoolDto poolDto = restTemplate.exchange(url, HttpMethod.POST, generateRequestEntity(reqDto), PoolDto.class)
                .getBody();
        assertNotNull(poolDto);
        assertEquals(reqDto.getName(), poolDto.getName());
        assertNotNull(poolDto.getId());

        String editId = poolDto.getEditId();

        // -- GET --
        poolDto = restTemplate.exchange(url + "/" + reqDto.getName(), HttpMethod.GET, generateRequestEntity(),
                                        PoolDto.class)
                .getBody();
        assertNotNull(poolDto);
        assertEquals(reqDto.getName(), poolDto.getName());
        assertNull(poolDto.getEditId());

        // -- PUT - FAIL --
        reqDto.setId(poolDto.getId());
        reqDto.setDescription("new description");
        try {
            final ResponseEntity<PoolDto> response = restTemplate.exchange(url, HttpMethod.PUT,
                                                                           generateRequestEntity(reqDto),
                                                                           PoolDto.class);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }

        // -- PUT - SUCCEED --
        reqDto.setId(poolDto.getId());
        reqDto.setEditId(editId);
        reqDto.setDescription("new description");
        poolDto = restTemplate.exchange(url, HttpMethod.PUT, generateRequestEntity(reqDto), PoolDto.class)
                .getBody();
        assertEquals(reqDto.getId(), poolDto.getId());
        assertEquals(reqDto.getDescription(), poolDto.getDescription());

        // -- DELETE --
        final ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE,
                                                                    generateRequestEntity(reqDto), Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        // -- GET --
        poolDto = restTemplate.exchange(url + "/" + reqDto.getName(), HttpMethod.GET, generateRequestEntity(),
                                        PoolDto.class)
                .getBody();
        assertNull(poolDto);
    }

    @Test
    public void domainTest() {
        String url = HOST_POOL;

        PoolDto reqDto = new PoolDto();
        reqDto.setName("elisaviihde.fi");
        reqDto.setDescription("description");

        // -- POST --
        PoolAccountDto poolAccountDto = null;
        try {
            poolAccountDto = restTemplate.exchange(url, HttpMethod.POST, generateRequestEntity(reqDto),
                                                   PoolAccountDto.class)
                    .getBody();
        } catch (HttpClientErrorException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        PoolDto poolDto = poolAccountDto.getPoolDto();
        assertNotNull(poolDto);
        assertEquals(reqDto.getName(), poolDto.getName());
        assertNotNull(poolDto.getId());
        assertNotNull(poolDto.getDomainVerificationCode());

        String editId = poolDto.getEditId();

        // -- GET --
        poolDto = restTemplate.exchange(url + "/" + reqDto.getName(), HttpMethod.GET, generateRequestEntity(),
                                        PoolDto.class)
                .getBody();
        assertNotNull(poolDto);
        assertEquals(reqDto.getName(), poolDto.getName());
        assertNull(poolDto.getEditId());
        assertNull(poolDto.getDomainVerificationCode());
    }

    private HttpEntity<?> generateRequestEntity(PoolDto req) {
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
