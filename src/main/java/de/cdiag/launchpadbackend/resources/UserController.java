package de.cdiag.launchpadbackend.resources;

import de.cdiag.launchpadbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@Slf4j
public class UserController {

    @GetMapping("login")
    public ResponseEntity<HttpStatus> login() {
        // http basic auth is handled in auth provider
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
