package de.cdiag.launchpadbackend.resources;

import de.cdiag.launchpadbackend.dto.UserDto;
import de.cdiag.launchpadbackend.message.Message;
import de.cdiag.launchpadbackend.message.ResponseMessage;
import de.cdiag.launchpadbackend.model.User;
import de.cdiag.launchpadbackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity<ResponseMessage> login(@RequestBody UserDto userDto) {

        final User user = new User(userDto.getUsername(), userDto.getPassword());
        if (userService.login(user)) {

            // http basic auth is handled in auth provider
            return new ResponseEntity<>(new ResponseMessage(Message.Status.OK, "Success"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseMessage(Message.Status.ERROR, "username or password invalid"), HttpStatus.UNAUTHORIZED);
        }

    }

    @GetMapping("error")
    public void error() {
        try {
            final FileInputStream fi = new FileInputStream("doesNotExists.file");
            final byte[] bytes = fi.readAllBytes();
            fi.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
