package com.socialmedia.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class NotFoundException extends ApiException {

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
        this.message = message;
    }

}
