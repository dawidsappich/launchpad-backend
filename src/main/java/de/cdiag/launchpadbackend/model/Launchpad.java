package de.cdiag.launchpadbackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor
@Data
@Entity
public class Launchpad {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private Template template;

    @OneToMany(mappedBy = "launchpad")
    private Set<Tile> tiles;

    @OneToOne(mappedBy = "launchpad")
    private User user;

    public Launchpad(Template template) {
        this.template = template;
    }
}
