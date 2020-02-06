package de.cdiag.launchpadbackend;

import de.cdiag.launchpadbackend.configuration.AwsConfig;
import de.cdiag.launchpadbackend.model.*;
import de.cdiag.launchpadbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import software.amazon.codeguruprofilerjavaagent.Profiler;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
@Slf4j
public class LaunchpadBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LaunchpadBackendApplication.class, args);
    }

}
