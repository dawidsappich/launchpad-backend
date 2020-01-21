package de.cdiag.launchpadbackend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = "tiles")
@Entity
public class Launchpad {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    private String title;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private Template template;

    @OneToMany(mappedBy = "launchpad", cascade = CascadeType.ALL)
    private Set<Tile> tiles;

    @OneToOne(mappedBy = "launchpad")
    private User user;

    public Launchpad(Template template) {
        this.template = template;
    }
}
