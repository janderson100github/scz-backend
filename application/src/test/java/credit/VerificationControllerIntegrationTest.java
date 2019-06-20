package credit;

import credit.model.AccountDto;
import credit.model.PoolAccountDto;
import credit.model.PoolDto;
import credit.model.VerificationDto;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

public class VerificationControllerIntegrationTest {

    private static final String HOST = "http://localhost:8080";

    private static final String HOST_VERIFICATION = HOST + Paths.VERIFICATION;

    private static final String HOST_POOL = HOST + Paths.POOL;

    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void generateAndValidate() {
        // -- new pool --
        PoolDto reqDto = new PoolDto();
        reqDto.setName(RandomStringUtils.randomAlphanumeric(32));
        reqDto.setDescription("description");
        PoolAccountDto poolAccountDto = restTemplate.exchange(HOST_POOL, HttpMethod.POST, generateRequestEntity(reqDto),
                                                              PoolAccountDto.class)
                .getBody();
        PoolDto poolDto = poolAccountDto.getPoolDto();
        AccountDto accountFromDto = poolAccountDto.getAccount();

        // -- POST email --
        String email = "j@j.com";
        VerificationDto verReqDto = new VerificationDto(poolDto.getPublicId(), email);
        VerificationDto verResDto = restTemplate.exchange(HOST_VERIFICATION + Paths.EMAIL, HttpMethod.POST,
                                                          generateRequestEntity(verReqDto), VerificationDto.class)
                .getBody();
        //assertNull(verResDto.getCode());

        // -- GET verify --
        String url = HOST_VERIFICATION + Paths.EMAIL + "/" + verResDto.getPoolPublicId() + "/" + verResDto.getCode();
        ResponseEntity<VerificationDto> responseEntity = restTemplate.exchange(url, HttpMethod.GET,
                                                                               generateRequestEntity(verReqDto),
                                                                               VerificationDto.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        VerificationDto verifiedDto = responseEntity.getBody();
    }

    private HttpEntity<?> generateRequestEntity(VerificationDto req) {
        return new HttpEntity<>(req, generateHeaders());
    }

    private HttpEntity<?> generateRequestEntity(PoolDto req) {
        return new HttpEntity<>(req, generateHeaders());
    }

    private HttpHeaders generateHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
