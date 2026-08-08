package com.speedtype.exception;

import org.springframework.http.HttpStatus;

/** A runtime exception carrying the HTTP status it should map to, so services can
 *  throw domain errors (e.g. "username taken") without depending on servlet types. */
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    public ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
