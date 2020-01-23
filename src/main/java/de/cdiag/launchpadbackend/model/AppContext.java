package de.cdiag.launchpadbackend.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@ToString
@EqualsAndHashCode(exclude = {"uuid", "created", "modified"})
@Getter
public class AppContext {

    private UUID uuid;
    private Long applicationId;
    private String applicationName;
    private Status status;
    private String executive;
    private LocalDateTime created;
    private LocalDateTime modified;

    public enum Status {
        IDLE, RUNNING, INTERRUPTED, STOPPED
    }

    public AppContext(Long applicationId, Status status, String executive) {
        this.uuid = UUID.randomUUID();
        this.applicationId = applicationId;
        this.status = status;
        this.executive = executive;
        this.created = LocalDateTime.now();
        this.modified = LocalDateTime.now();
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }
}
