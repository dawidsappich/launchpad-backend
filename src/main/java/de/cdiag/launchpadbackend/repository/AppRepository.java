package de.cdiag.launchpadbackend.repository;

import de.cdiag.launchpadbackend.model.App;
import org.springframework.data.repository.CrudRepository;

public interface AppRepository extends CrudRepository<App, Long> {
}
