package de.cdiag.launchpadbackend;

import de.cdiag.launchpadbackend.aws.AwsConfig;
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

    private final AwsConfig awsConfig;
    private final Profiler prof;
    @Autowired
    private UserService userService;


    @Autowired
    public LaunchpadBackendApplication(final AwsConfig _awsConfig) {
        this.awsConfig = _awsConfig;
        prof = this.awsConfig.awsCodeGuruProfiler();
        prof.start();
    }

    public static void main(String[] args) {
        SpringApplication.run(LaunchpadBackendApplication.class, args);
    }

    @PreDestroy
    private void stop() {
        log.info("Stopping Profiler");
        if (prof.isRunning()) {
            prof.stop();
        }
    }

    // seed data to database
    @Bean
    CommandLineRunner init() {
        return args -> {

            final Launchpad launchpad = new Launchpad();
            launchpad.setTitle("MyHome");
            final Template template = new Template("generic", "template for business applications");
            launchpad.setTemplate(template);

            // applications
            final App invoices = new App("Invoices", "customer invoices");
            final App schedules = new App("Schedules", "Show the upcoming schedules");
            final App warehouse = new App("Warehouse", "Manage items in the warehouse");
            final App operating = new App("Operating", "Contact the service help");

            Set<App> apps = new HashSet<>();
            apps.add(invoices);

            template.setApplications(apps);

            final Tile invoiceTile = new Tile("my invoices", "all invoices in progress", invoices);
            invoiceTile.setIcon("invoice-icon");
            // set relationship to launchpad
            invoiceTile.setLaunchpad(launchpad);
            Set<Tile> tiles = new HashSet<>();
            tiles.add(invoiceTile);
            launchpad.setTiles(tiles);


            // users
            User qualityAssurance = new User("qa", "pass");
            User productOwner = new User("po", "pass");
            User normalUser = new User("user", "pass");

            // add a launchpad for the normal user
            normalUser.setLaunchpad(launchpad);

            List<User> users = new ArrayList<>();
            users.add(qualityAssurance);
            users.add(productOwner);
            users.add(normalUser);

            userService.save(users);


        };
    }
}
