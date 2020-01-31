package de.cdiag.launchpadbackend.service;

import de.cdiag.launchpadbackend.exception.NotFoundException;
import de.cdiag.launchpadbackend.exception.UserAlreadyExistsException;
import de.cdiag.launchpadbackend.model.*;
import de.cdiag.launchpadbackend.repository.AppRepository;
import de.cdiag.launchpadbackend.repository.LaunchpadRepository;
import de.cdiag.launchpadbackend.repository.TemplateRepository;
import de.cdiag.launchpadbackend.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final LaunchpadRepository launchpadRepository;
    private final TemplateRepository templateRepository;
    private final AppRepository appRepository;

    public UserService(UserRepository userRepository, LaunchpadRepository launchpadRepository, TemplateRepository templateRepository, AppRepository appRepository) {
        this.userRepository = userRepository;
        this.launchpadRepository = launchpadRepository;
        this.templateRepository = templateRepository;
        this.appRepository = appRepository;
    }

    @Bean
    private PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public User saveUser(final User user) {
        String encodedPassword = encodePassword(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    protected String encodePassword(final String password) {
        return passwordEncoder().encode(password);
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        // load user from repository
        final User user = getUser(username);

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(AuthorityUtils.createAuthorityList("USER"))
                .build();
    }

    public boolean matches(final String username, final String rawPassword) {
        Optional<User> byUsername = userRepository.findByUsername(username);

        if (byUsername.isEmpty()) {
            return false;
        }

        @NotNull @NotBlank final String userPassword = byUsername.get().getPassword();
        // delegate the comparison to the password encoder
        // compare the raw password (not encoded) with the hashed password
        // passwordencoder will select the right encoder for comparision
        return passwordEncoder().matches(rawPassword, userPassword);
    }

    public Iterable<? extends User> save(List<? extends User> users) {
        // hash all passwords
        users.forEach(user -> user.setPassword(encodePassword(user.getPassword())));
        return userRepository.saveAll(users);
    }

    public Launchpad loadLaunchpad(String username) {
        final User user = getUser(username);
        final Optional<Launchpad> lunchpadById = launchpadRepository.findById(user.getLaunchpad().getId());

        if (lunchpadById.isEmpty()) {
            // TODO handle exceptions
        }
        return lunchpadById.get();
    }


    private User getUser(String username) {
        final Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isEmpty()) {
            // error is handled in RestResponseEntityExceptionHandler
            throw new UsernameNotFoundException("user with username '" + username + "' not found");
        }
        return byUsername.get();
    }

    public boolean login(User providedUser) {
        @NotNull @NotBlank final String username = providedUser.getUsername();
        final User user = getUser(username);
        return matches(username, providedUser.getPassword());
    }

    public void saveTemplates(Set<Template> templates) {
        templateRepository.saveAll(templates);
    }

    public Iterable<Template> loadTemplates() {
        return templateRepository.findAll();
    }

    // add transaction handling
    public Tile addTileToUserLaunchpad(Template template, String userName) {

        final Launchpad launchpad = loadLaunchpad(userName);
        final Set<App> applications = template.getApplications();
        final App app = applications.stream()
                .findFirst()
                // TODO handle exception gracefully
                .orElseThrow(RuntimeException::new);

        // find the app
        final Optional<App> appById = appRepository.findById(app.getId());
        final App foundApp = appById.orElseThrow(NotFoundException::new);

        final Tile tile = createTile(template, launchpad, foundApp);

        // add tile to launchpad
        final Set<Tile> tiles = launchpad.getTiles();
        tiles.add(tile);

        // save launchpad (or user?)
        final Launchpad launchpad_updated = launchpadRepository.save(launchpad);
        // return the tile form the launchpad

        return tile;
    }

    private Tile createTile(Template template, Launchpad launchpad, App app) {
        // create tile
        // create relationship between tile and application
        final Tile tile = new Tile(template.getTemplateName(), template.getTemplateDescription(), app);
        // create relationship to launchpad
        tile.setLaunchpad(launchpad);
        tile.setIcon("icon-operating");
        return tile;
    }

    public App updateApplication(App providedApp) {
        // find the application
        final Optional<App> appById = appRepository.findById(providedApp.getId());
        if (appById.isEmpty()) {
            throw new NotFoundException("application with id: " + providedApp.getId() + " not found");
        }

        // update the application
        final App app = appById.get();
        app.setAppName(providedApp.getAppName());
        app.setAppDescription(providedApp.getAppDescription());

        //persist the application
        final App savedApp = appRepository.save(app);

        //return the updated
        return savedApp;
    }

    public User register(User user) {
        // check if user already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("username '" + user.getUsername() + "' already exists.");
        }
        user.setPassword(encodePassword(user.getPassword()));
        return userRepository.save(user);
    }
}
