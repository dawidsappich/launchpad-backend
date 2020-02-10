package de.cdiag.launchpadbackend.repository;

import de.cdiag.launchpadbackend.model.Tile;
import org.springframework.data.repository.CrudRepository;

public interface TileRepository extends CrudRepository<Tile, Long> {
}
