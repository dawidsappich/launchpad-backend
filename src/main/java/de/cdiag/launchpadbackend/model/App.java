package de.cdiag.launchpadbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"templates","tile"})
@Entity
public class App {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String appName;

    private String appDescription;

    @ManyToMany(mappedBy = "applications")
    private Set<Template> templates;

    @OneToOne(mappedBy = "application")
    private Tile tile;

    public App(String appName, String appDescription) {
        this.appName = appName;
        this.appDescription = appDescription;
    }
}
