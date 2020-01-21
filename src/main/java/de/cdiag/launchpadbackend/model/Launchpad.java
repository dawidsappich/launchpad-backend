package de.cdiag.launchpadbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "tiles")
@JsonIgnoreProperties({"user"})
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
