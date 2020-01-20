package de.cdiag.launchpadbackend;

import de.cdiag.launchpadbackend.model.User;
import de.cdiag.launchpadbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
@Slf4j
public class LaunchpadBackendApplication {

    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(LaunchpadBackendApplication.class, args);
    }

    // seed users to database
    @Bean
    CommandLineRunner init() {
        return args -> {
            User qualityAssurance = new User("qa", "pass");
            User productOwner = new User("po", "pass");
            userService.saveUser(qualityAssurance);
            userService.saveUser(productOwner);
        };
    }
}
