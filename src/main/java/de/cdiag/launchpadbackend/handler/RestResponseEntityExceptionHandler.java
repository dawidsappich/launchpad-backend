package de.cdiag.launchpadbackend.handler;

import de.cdiag.launchpadbackend.message.ResponseMessage;
import org.springframework.context.ApplicationContextException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class, ApplicationContextException.class})
    protected ResponseEntity<ResponseMessage> handleConflict(RuntimeException ex, WebRequest request) {
        final var message = new ResponseMessage(ResponseMessage.Status.ERROR, ex.getMessage());
        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }
}
