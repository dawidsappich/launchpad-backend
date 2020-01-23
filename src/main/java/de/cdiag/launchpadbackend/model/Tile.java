package de.cdiag.launchpadbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"launchpad"})
@Entity
public class Tile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @NotNull @NotBlank
    private String title;

    private String description;

    private String icon;

    @OneToOne(cascade = CascadeType.ALL)
    private App application;

    @ManyToOne(fetch = FetchType.EAGER)
    private Launchpad launchpad;

    public Tile(String title, String description, App application) {
        this.title = title;
        this.description = description;
        this.application = application;
    }
}
