package de.cdiag.launchpadbackend.message;

import lombok.Data;

@Data
public abstract class Message {

    protected Status status;
    protected String message;

    public enum Status {
        OK, WARN, ERROR
    }
}
