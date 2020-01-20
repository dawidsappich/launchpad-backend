package de.cdiag.launchpadbackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@Data
@Entity
public class App {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String appName;

    private String appDescription;

    @ManyToMany(mappedBy = "applications")
    private Set<Template> templates;

    @OneToOne(mappedBy = "application")
    private Tile tile;

}
