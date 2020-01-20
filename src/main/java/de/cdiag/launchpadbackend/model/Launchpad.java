package de.cdiag.launchpadbackend.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Set;

public class Launchpad {

    private long id;


    private Template template;
    private Set<Tile> tiles;
}
