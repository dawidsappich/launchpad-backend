package de.cdiag.launchpadbackend.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.cdiag.launchpadbackend.message.Message;
import de.cdiag.launchpadbackend.message.ResponseMessage;
import de.cdiag.launchpadbackend.model.AppContext;
import de.cdiag.launchpadbackend.model.Launchpad;
import de.cdiag.launchpadbackend.model.Template;
import de.cdiag.launchpadbackend.model.Tile;
import de.cdiag.launchpadbackend.service.AppExecutorService;
import de.cdiag.launchpadbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    //  ##### TILE #####

    @GetMapping("tile/all")
    public ResponseEntity<Launchpad> getAllUserTiles() {
        // get the username from the security context
        final String username = getUserName();

        Launchpad launchpad = userService.loadLaunchpad(username);

        return new ResponseEntity<>(launchpad, HttpStatus.OK);
    }

    @PostMapping("tile/add")
    public ResponseEntity<ResponseMessage> addTile(@RequestBody Template template) {

        final String userName = getUserName();
        final Tile tile = userService.addTileToUserLaunchpad(template, userName);
        String payload;
        try {
            payload = asJson(tile);
        } catch (JsonProcessingException e) {
            return handleException(e);
        }

        final ResponseMessage response = new ResponseMessage(Message.Status.OK, "Success", payload);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    //  ##### APPLICATION #####

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

    //  ##### TEMPLATE #####

    @GetMapping("template/all")
    public ResponseEntity<ResponseMessage> getAllTemplates() {

        final Iterable<Template> templates = userService.loadTemplates();
        String payload;

        try {

            payload = asJson(templates);

        } catch (JsonProcessingException ex) {
            return handleException(ex);
        }

        final ResponseMessage response = new ResponseMessage(Message.Status.OK, "Success", payload);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    private ResponseEntity<ResponseMessage> handleException(Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    // add templates (applications) for the launchpad
    private String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private String asJson(Object obj) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
}
