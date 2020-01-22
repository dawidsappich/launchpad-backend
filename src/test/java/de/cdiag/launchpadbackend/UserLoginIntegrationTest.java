package de.cdiag.launchpadbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.cdiag.launchpadbackend.message.ResponseMessage;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserLoginIntegrationTest {

    public static final String USER_LOGIN_URL = "/api/v1/user/login";
    public static final String HOST = "http://localhost:";
    TestRestTemplate restTemplate;
    URL userLoginURL;
    @LocalServerPort int port;
    ObjectMapper mapper;

    @BeforeEach
    private void setup() throws MalformedURLException {
        restTemplate = new TestRestTemplate();
        userLoginURL = new URL(HOST + port + USER_LOGIN_URL);
        mapper = new ObjectMapper();
    }

    @Test
    void givenCredentials_whenLogin_thenResponseMessageStatusSuccess() throws JSONException, MalformedURLException {
        // given
        final JSONObject userJSON = new JSONObject();
        userJSON.put("username", "user");
        userJSON.put("password", "pass");

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final HttpEntity<String> entity = new HttpEntity<>(userJSON.toString(), headers);

        // when
        final ResponseMessage message = restTemplate.postForObject(userLoginURL.toString(), entity, ResponseMessage.class);

        //then
        assertNotNull(message, "response must be present");
        assertEquals(ResponseMessage.Status.OK, message.getStatus(), "Status must be 'OK'");

    }

    @Test
    void givenBadCredentials_whenLogin_thenResponseMessageStatusError() throws JSONException {
        // given
        final JSONObject userJSON = new JSONObject();
        userJSON.put("username", "unknownUser");
        userJSON.put("password", "pass");

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final HttpEntity<String> entity = new HttpEntity<>(userJSON.toString(), headers);

        // when
        final ResponseMessage message = restTemplate.postForObject(userLoginURL.toString(), entity, ResponseMessage.class);

        //then
        assertNotNull(message, "response must be present");
        assertEquals(ResponseMessage.Status.ERROR, message.getStatus(), "status must be 'ERROR'");
    }
}
