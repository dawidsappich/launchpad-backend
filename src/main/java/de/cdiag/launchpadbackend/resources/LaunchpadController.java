package de.cdiag.launchpadbackend.resources;

import de.cdiag.launchpadbackend.model.Launchpad;
import de.cdiag.launchpadbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("launchpad")
@Slf4j
public class LaunchpadController {

    private final UserService userService;

    public LaunchpadController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("load")
    public ResponseEntity<Launchpad> getAllUserTiles() {
        // get the username from the security context
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Launchpad launchpad = userService.loadLaunchpad(username);

        return new ResponseEntity<>(launchpad, HttpStatus.OK);
    }
}
