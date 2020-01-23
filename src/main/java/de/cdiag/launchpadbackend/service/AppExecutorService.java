package de.cdiag.launchpadbackend.service;

import de.cdiag.launchpadbackend.model.App;
import de.cdiag.launchpadbackend.model.AppContext;
import de.cdiag.launchpadbackend.repository.AppRepository;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AppExecutorService {

    private final AppRepository appRepository;
    private final Map<String, List<AppContext>> executorContext;

    public AppExecutorService(AppRepository appRepository) {
        this.executorContext = new ConcurrentHashMap<>();
        this.appRepository = appRepository;
    }

    public AppContext startApplication(Long applicationId, String executive) {
        AppContext context = createAppContext(applicationId, executive);
        execute(context);
        return context;
    }

    private void execute(AppContext context) {
        executorContext.putIfAbsent(context.getExecutive(), new ArrayList<>());
        final List<AppContext> appContexts = executorContext.get(context.getExecutive());

        if (appContexts.indexOf(context) == -1) {
            appContexts.add(context);
        } else {
            throw new ApplicationContextException("application with id: " + context.getApplicationId() + " is already running");
        }
    }

    private AppContext createAppContext(Long applicationId, String executive) {
        final Optional<App> appNyId = appRepository.findById(applicationId);
        if (appNyId.isEmpty()) {
            throw new ApplicationContextException("application not found");
        }

        final App app = appNyId.get();
        return new AppContext(app.getId(), AppContext.Status.RUNNING, executive);
    }
}
