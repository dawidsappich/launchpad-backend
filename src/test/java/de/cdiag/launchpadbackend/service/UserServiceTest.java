package de.cdiag.launchpadbackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.cdiag.launchpadbackend.dto.UserDto;
import de.cdiag.launchpadbackend.message.Message;
import de.cdiag.launchpadbackend.message.ResponseMessage;
import de.cdiag.launchpadbackend.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceTest {


    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserService userService;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void givenRawPassword_whenEncodingPassword_thenPasswordMustNotMatchRawPassword() {
        // given
        final String rawPassword = "password";

        // when
        final String encoded = userService.encodePassword(rawPassword);

        // then
        assertNotEquals(rawPassword, encoded, "password and encoded password must not match");
    }


    // ########## E2E Test ###########
    @Test
    void givenUser_whenSaveNewUsername_thenReturnNewUser() {
        // given
        final UserDto userDto = new UserDto("newuser", "password");

        // given
        final ResponseMessage message = restTemplate.postForObject("http://localhost:" + port + "/api/v1/user/signup", userDto, ResponseMessage.class);

        // then
        assertEquals(ResponseMessage.Status.OK, message.getStatus(), "status must be 'OK'");
        assertEquals("Success", message.getMessage(), "message must be 'Success'");
    }

    @Test
    void givenUser_whenSaveExistingUsername_thenReturnError() {
        // given
        final UserDto userDto = new UserDto("newuser", "password");

        // given
        final ResponseMessage message = restTemplate.postForObject("http://localhost:" + port + "/api/v1/user/signup", userDto, ResponseMessage.class);

        // then
        assertEquals(ResponseMessage.Status.ERROR, message.getStatus(), "status must be 'ERROR'");
        assertEquals("username 'newuser' already exists.", message.getMessage(), "message must match");
    }
}