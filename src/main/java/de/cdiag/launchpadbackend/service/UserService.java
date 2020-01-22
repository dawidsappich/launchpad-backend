package de.cdiag.launchpadbackend.service;

import de.cdiag.launchpadbackend.model.Launchpad;
import de.cdiag.launchpadbackend.model.User;
import de.cdiag.launchpadbackend.repository.LaunchpadRepository;
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

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final LaunchpadRepository launchpadRepository;

    public UserService(final UserRepository userRepository, LaunchpadRepository launchpadRepository) {
        this.userRepository = userRepository;
        this.launchpadRepository = launchpadRepository;
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

    private String encodePassword(final String password) {
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
            // TODO handle exceptions
            throw new UsernameNotFoundException("user with username '" + username + "' not found");
        }
        return byUsername.get();
    }

    public boolean login(User providedUser) {
        @NotNull @NotBlank final String username = providedUser.getUsername();
        final User user = getUser(username);
        return matches(username, providedUser.getPassword());
    }
}
