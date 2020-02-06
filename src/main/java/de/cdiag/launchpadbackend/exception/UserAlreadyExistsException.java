package de.cdiag.launchpadbackend.exception;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super();
    }

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    protected UserAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UserAlreadyExistsException(@NotNull @NotBlank String message) {
        super(message);
    }
}
