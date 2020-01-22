package de.cdiag.launchpadbackend.message;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ResponseMessage {
    private Status status;
    private String message;

    public enum Status {
        OK, WARN, ERROR
    }

    public ResponseMessage(Status status, String message) {
        this.status = status;
        this.message = message;
    }
}
