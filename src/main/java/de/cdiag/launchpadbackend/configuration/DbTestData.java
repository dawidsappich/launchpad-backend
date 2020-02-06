package de.cdiag.launchpadbackend.configuration;

import de.cdiag.launchpadbackend.model.*;
import de.cdiag.launchpadbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@Profile("dev")
public class DbTestData {

    @Autowired
    private UserService userService;

    // seed data to database
    @Bean
    CommandLineRunner init() {
        return args -> {

            final Launchpad launchpad = new Launchpad();
            launchpad.setTitle("My Applications");
            final Template invoiceTemplate = new Template("invoice", "accounting");
            final Template scheduleTemplate = new Template("schedule", "schedules");
            final Template warehouseTemplate = new Template("warehouse", "warehouse");
            final Template operatingTemplate = new Template("operating", "operating");

            // collect templates into collection to be persisted
            // these templates are not related to a launchpad
            // they will be used for assigning to a new launchpad
            Set<Template> templates = new HashSet<>();
            templates.add(scheduleTemplate);
            templates.add(warehouseTemplate);
            templates.add(operatingTemplate);


            // create a relationship (the hole chain) for the invoice template to a user
            launchpad.setTemplate(invoiceTemplate);

            // applications
            final App invoices = new App("Invoices", "Customer invoices");
            final App schedules = new App("Schedules", "Show the upcoming schedules");
            final App warehouse = new App("Warehouse", "Manage items in the warehouse");
            final App operating = new App("Operating", "Contact the service help");

            Set<App> apps = new HashSet<>();
            apps.add(invoices);

            invoiceTemplate.setApplications(apps);

            Set<App> remainingApps = new HashSet<>();
            // add app to the collection
            remainingApps.add(schedules);

            // create relationship for the app collection to a template
            scheduleTemplate.setApplications(remainingApps);

            remainingApps = new HashSet<>();
            remainingApps.add(warehouse);

            warehouseTemplate.setApplications(remainingApps);

            remainingApps = new HashSet<>();
            remainingApps.add(operating);

            operatingTemplate.setApplications(remainingApps);

            final Tile invoiceTile = new Tile("my invoices", "all invoices in progress", invoices);
            invoiceTile.setIcon("faFileInvoice");
            // set relationship to launchpad
            invoiceTile.setLaunchpad(launchpad);
            Set<Tile> tiles = new HashSet<>();
            tiles.add(invoiceTile);
            launchpad.setTiles(tiles);


            // users
            User qualityAssurance = new User("qa", "pass");
            User productOwner = new User("po", "pass");
            User normalUser = new User("user", "pass");
            User adminUser = new User("admin", "pass");

            // add a launchpad for the normal user
            normalUser.setLaunchpad(launchpad);

            List<User> users = new ArrayList<>();
            users.add(qualityAssurance);
            users.add(productOwner);
            users.add(normalUser);
            users.add(adminUser);

            userService.save(users);
            userService.saveTemplates(templates);

        };
    }
}
