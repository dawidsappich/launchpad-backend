package de.cdiag.launchpadbackend.service;

import de.cdiag.launchpadbackend.model.Launchpad;
import de.cdiag.launchpadbackend.repository.LaunchpadRepository;
import org.springframework.stereotype.Service;

@Service
public class LaunchpadService {

    private final LaunchpadRepository launchpadRepository;

    public LaunchpadService(LaunchpadRepository launchpadRepository) {
        this.launchpadRepository = launchpadRepository;
    }

    public void save(Launchpad launchpad) {
        launchpadRepository.save(launchpad);
    }
}
