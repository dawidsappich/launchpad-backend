package de.cdiag.launchpadbackend.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.cdiag.launchpadbackend.message.Message;
import de.cdiag.launchpadbackend.message.ResponseMessage;
import de.cdiag.launchpadbackend.model.AppContext;
import de.cdiag.launchpadbackend.model.Launchpad;
import de.cdiag.launchpadbackend.model.Template;
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

import java.util.List;

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
        final String username = getUserName();

        Launchpad launchpad = userService.loadLaunchpad(username);

        return new ResponseEntity<>(launchpad, HttpStatus.OK);
    }

    @GetMapping("application/{id}")
    public ResponseEntity<ResponseMessage> startApplication(@PathVariable Long id) {

        // get the username from the security context
        final String username = getUserName();
        // start the application
        final AppContext context = appExecutorService.startApplication(id, username);

        final ResponseMessage response = new ResponseMessage(Message.Status.OK, "started");
        response.setPayload(context.toString());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("template/all")
    public ResponseEntity<ResponseMessage> getAllTemplates() {

        final Iterable<Template> templates = userService.loadTemplates();
        String payload;

        try {

            final ObjectMapper mapper = new ObjectMapper();
            payload = mapper.writeValueAsString(templates);

        } catch (JsonProcessingException ex) {
            log.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        final ResponseMessage response = new ResponseMessage(Message.Status.OK, "Success", payload);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    // add templates (applications) for the launchpad
    private String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
