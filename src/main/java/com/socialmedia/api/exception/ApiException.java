package com.socialmedia.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {
    private static final String GENERIC_ERROR_MESSAGE = "An error occurred, please try again";
    protected String message = GENERIC_ERROR_MESSAGE;
    protected HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public ApiException(String message) {
        super(message);
        this.message = message;
    }

    public ApiException() {
        super(GENERIC_ERROR_MESSAGE);
    }

    public ApiException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
