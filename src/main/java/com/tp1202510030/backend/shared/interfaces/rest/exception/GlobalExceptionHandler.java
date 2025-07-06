package com.tp1202510030.backend.shared.interfaces.rest.exception;

import com.tp1202510030.backend.shared.interfaces.rest.resources.ErrorResponseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseResource> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        logger.warn("Client error - Bad Request (404): {}. Request: {}", ex.getMessage(), request.getDescription(false));
        var errorResponse = new ErrorResponseResource(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getDescription(false),
                new Date()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseResource> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
        logger.warn("Client error - Bad Request (400): {}. Request: {}", ex.getMessage(), request.getDescription(false));

        var errorResponse = new ErrorResponseResource(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getDescription(false),
                new Date()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseResource> handleGlobalException(Exception ex, WebRequest request) {
        logger.error("An unexpected internal server error occurred. Request: {}", request.getDescription(false), ex);

        var errorResponse = new ErrorResponseResource(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocurrió un error interno en el servidor.",
                request.getDescription(false),
                new Date()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}