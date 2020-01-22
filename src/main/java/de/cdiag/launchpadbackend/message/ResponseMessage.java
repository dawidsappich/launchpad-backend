package de.cdiag.launchpadbackend.message;

import lombok.Data;

@Data
public class ResponseMessage {
    private final Status status;
    private final String message;

    public enum Status {
        OK, WARN, ERROR
    }
}
