package de.cdiag.launchpadbackend.resources;

import de.cdiag.launchpadbackend.message.Message;
import de.cdiag.launchpadbackend.message.ResponseMessage;
import de.cdiag.launchpadbackend.model.AppContext;
import de.cdiag.launchpadbackend.model.Launchpad;
import de.cdiag.launchpadbackend.service.AppExecutorService;
import de.cdiag.launchpadbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("launchpad")
@Slf4j
public class LaunchpadController {

    private final UserService userService;
    private final AppExecutorService appExecutorService;

    public LaunchpadController(UserService userService, AppExecutorService appExecutorService) {
        this.userService = userService;
        this.appExecutorService = appExecutorService;
    }

    @GetMapping("load")
    public ResponseEntity<Launchpad> getAllUserTiles() {
        // get the username from the security context
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Launchpad launchpad = userService.loadLaunchpad(username);

        return new ResponseEntity<>(launchpad, HttpStatus.OK);
    }

    @GetMapping("application/{id}")
    public ResponseEntity<ResponseMessage> startApplication(@PathVariable Long id) {

        // get the username from the security context
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // start the application
        final AppContext context = appExecutorService.startApplication(id, username);

        final ResponseMessage response = new ResponseMessage(Message.Status.OK, "started");
        response.setPayload(context.toString());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // add templates (applications) for the launchpad

}
