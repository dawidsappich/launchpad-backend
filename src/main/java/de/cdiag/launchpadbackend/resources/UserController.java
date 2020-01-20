package de.cdiag.launchpadbackend.resources;

import de.cdiag.launchpadbackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("login")
    public ResponseEntity<HttpStatus> login() {
        // http basic auth is handled in auth provider
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);

    }
}
