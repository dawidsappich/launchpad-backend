package de.cdiag.launchpadbackend.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.cdiag.launchpadbackend.message.Message;
import de.cdiag.launchpadbackend.message.ResponseMessage;
import de.cdiag.launchpadbackend.model.*;
import de.cdiag.launchpadbackend.service.AppExecutorService;
import de.cdiag.launchpadbackend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("launchpad")
@CrossOrigin
@Slf4j
@Api(description = "operations regarding templates, tiles, apps and the launchpad")
public class LaunchpadController {

    private final UserService userService;
    private final AppExecutorService appExecutorService;

    public LaunchpadController(UserService userService, AppExecutorService appExecutorService) {
        this.userService = userService;
        this.appExecutorService = appExecutorService;
    }

    //  ##### TILE #####

    @ApiOperation("load all tiles for the user")
    @GetMapping("tile/all")
    public ResponseEntity<Launchpad> getAllUserTiles() {
        // get the username from the security context
        final String username = getUserName();

        Launchpad launchpad = userService.loadLaunchpad(username);

        return new ResponseEntity<>(launchpad, HttpStatus.OK);
    }

    @ApiOperation("add the tile from the template to the launchpad of the user")
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

    @ApiOperation("update the tile metadata")
    @PatchMapping("tile")
    public ResponseEntity<ResponseMessage> updateTile(@RequestBody Tile tile) {

        // get the username from the security context
        final String username = getUserName();

        final Tile updatedTile = userService.updateTile(tile);

        String payload = null;
        try {
            payload = asJson(updatedTile);
        } catch (JsonProcessingException e) {
            handleException(e);
        }

        final ResponseMessage response = new ResponseMessage(Message.Status.OK, "tile updated", payload);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    //  ##### APPLICATION #####

    @ApiOperation("start the application for the provided application id")
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
    @ApiOperation("load all templates for the user")
    @GetMapping("template/all")
    public ResponseEntity<Iterable<Template>> getAllTemplates() {

        final Iterable<Template> templates = userService.loadTemplates();

        return new ResponseEntity<>(templates, HttpStatus.OK);

    }

    private ResponseEntity<ResponseMessage> handleException(Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // inject or inintialize one time as static
    private String asJson(Object obj) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    // ###### USERROLE ######

    @ApiOperation("get a flag if the user is an admin")
    @GetMapping("userrole")
    public ResponseEntity<Boolean> isUserRoleAdmin() {

        final String userName = getUserName();
        boolean isAdmin = userName.equals("user");

        return new ResponseEntity<>(isAdmin, HttpStatus.OK);
    }
}
