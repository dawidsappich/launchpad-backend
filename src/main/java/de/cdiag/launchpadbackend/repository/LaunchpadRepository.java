package de.cdiag.launchpadbackend.repository;

import de.cdiag.launchpadbackend.model.Launchpad;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LaunchpadRepository extends CrudRepository<Launchpad, Long> {
}
