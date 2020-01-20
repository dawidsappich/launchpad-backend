package de.cdiag.launchpadbackend.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Tile {

    private long id;
    private String title;
    private String description;
    private String icon;
    private App application;
}
