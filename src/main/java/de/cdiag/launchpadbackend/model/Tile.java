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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull @NotBlank
    private String title;

    private String description;

    private String icon;

    @OneToOne
    private App application;

    @ManyToOne
    private Launchpad launchpad;
}
