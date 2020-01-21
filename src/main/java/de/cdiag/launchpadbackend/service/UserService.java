package de.cdiag.launchpadbackend.service;

import de.cdiag.launchpadbackend.model.User;
import de.cdiag.launchpadbackend.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
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
        Optional<User> byUsername = userRepository.findByUsername(username);

        if (byUsername.isEmpty()) {
            throw new UsernameNotFoundException("username not found");
        }

        User user = byUsername.get();

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

}
