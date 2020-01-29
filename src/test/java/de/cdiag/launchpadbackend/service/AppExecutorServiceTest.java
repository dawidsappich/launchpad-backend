package de.cdiag.launchpadbackend.service;

import de.cdiag.launchpadbackend.model.App;
import de.cdiag.launchpadbackend.model.AppContext;
import de.cdiag.launchpadbackend.repository.AppRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
class AppExecutorServiceTest {

    @MockBean
    private AppRepository appRepository;

    @Autowired
    private AppExecutorService executorService;

    @Test
    void givenAppIdAndExecutive_whenStartApp_thenCreateApplicationContext() {
        when(appRepository.findById(1L))
                .thenReturn(Optional.of(new App("test app", "an application for testing purposes")));

        final AppContext appContext = executorService.startApplication(1L, "unit tester");

        assertNotNull(appContext);
        assertEquals("test app", appContext.getApplicationName());
    }
}