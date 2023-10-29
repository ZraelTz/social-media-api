package com.socialmedia.api.controller;

import com.fasterxml.jackson.core.JacksonException;
import com.socialmedia.api.dto.response.ApiResponse;
import com.socialmedia.api.exception.ApiException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@ControllerAdvice
@Slf4j
public class ControllerAdviceHandler {

    @ExceptionHandler(ApiException.class)
    public final ResponseEntity<ApiResponse<Boolean>> handleApiException(ApiException ex, WebRequest request) {
        return ResponseEntity.status(ex.getHttpStatus())
                .body(ApiResponse.<Boolean>builder()
                        .message(ex.getLocalizedMessage())
                        .data(false)
                        .build());
    }

    @ExceptionHandler(JacksonException.class)
    public final ResponseEntity<ApiResponse<Boolean>> handleJacksonException(JacksonException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Boolean>builder()
                        .message("Invalid json request")
                        .data(false)
                        .build());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public final ResponseEntity<ApiResponse<Boolean>> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(ApiResponse.<Boolean>builder()
                        .message(ex.getLocalizedMessage())
                        .data(false)
                        .build());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public final ResponseEntity<ApiResponse<Boolean>> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(ApiResponse.<Boolean>builder()
                        .message(ex.getLocalizedMessage())
                        .data(false)
                        .build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<ApiResponse<Boolean>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Boolean>builder()
                        .message(ex.getLocalizedMessage())
                        .data(false)
                        .build());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public final ResponseEntity<ApiResponse<Boolean>> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex, WebRequest request) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(ApiResponse.<Boolean>builder()
                        .message(ex.getLocalizedMessage())
                        .data(false)
                        .build());
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ApiResponse<Boolean>> handleAuthException(AuthenticationException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.<Boolean>builder()
                        .message(ex.getLocalizedMessage())
                        .data(false)
                        .build());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Boolean>> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.<Boolean>builder()
                        .message("Please provide the missing parameter: " + ex.getParameterName())
                        .data(false)
                        .build());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Boolean>> processValidationError(final MethodArgumentNotValidException ex) {
        String fieldError = ex.getBindingResult().getAllErrors().stream().findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toString();

        //clean field value which is in the form Optional[error_statement]
        fieldError = fieldError.replace("Optional[", "");
        fieldError = fieldError.replace("]", "");
        log.error("field error => {}", fieldError);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Boolean>builder()
                        .message(fieldError)
                        .data(false)
                        .build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Boolean>> handleConstraintViolationException(ConstraintViolationException constraintViolationException) {
        Optional<ConstraintViolation<?>> violations = constraintViolationException
                .getConstraintViolations().stream()
                .findFirst();

        log.error(constraintViolationException.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage).toList().toString());

        String errorMessage;
        if (violations.isPresent()) {
            errorMessage = violations.get().getMessage();
        } else {
            errorMessage = "An error occurred.";
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Boolean>builder()
                        .message(errorMessage)
                        .data(false)
                        .build());
    }

}
