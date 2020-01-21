package de.cdiag.launchpadbackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
@Entity
public class Tile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull @NotBlank
    private String title;

    private String description;

    private String icon;

    @OneToOne(cascade = CascadeType.ALL)
    private App application;

    @ManyToOne
    private Launchpad launchpad;

    public Tile(String title, String description, App application) {
        this.title = title;
        this.description = description;
        this.application = application;
    }
}
