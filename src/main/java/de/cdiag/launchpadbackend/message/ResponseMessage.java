package de.cdiag.launchpadbackend.message;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ResponseMessage extends Message {

    private String payload;

    public ResponseMessage(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResponseMessage(Status status, String message, String payload) {
        this(status, message);
        this.payload = payload;
    }
}
