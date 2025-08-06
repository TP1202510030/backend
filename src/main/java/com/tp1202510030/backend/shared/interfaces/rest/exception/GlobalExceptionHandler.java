package com.tp1202510030.backend.shared.interfaces.rest.exception;

import com.tp1202510030.backend.shared.domain.exceptions.ResourceAlreadyExistsException;
import com.tp1202510030.backend.shared.domain.exceptions.ResourceNotFoundException;
import com.tp1202510030.backend.shared.interfaces.rest.resources.ErrorResponseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseResource> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        logger.warn("Resource Not Found (404): {}. Request: {}", ex.getMessage(), request.getDescription(false));
        var errorResponse = new ErrorResponseResource(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getDescription(false),
                new Date()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseResource> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex, WebRequest request) {
        logger.warn("Resource Already Exists (409): {}. Request: {}", ex.getMessage(), request.getDescription(false));
        var errorResponse = new ErrorResponseResource(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                request.getDescription(false),
                new Date()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

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

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseResource> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        logger.warn("Access Denied (403): {}. Request: {}", ex.getMessage(), request.getDescription(false));
        var errorResponse = new ErrorResponseResource(
                HttpStatus.FORBIDDEN.value(),
                "Access Denied. You do not have permission to access this resource.",
                request.getDescription(false),
                new Date()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseResource> handleGlobalException(Exception ex, WebRequest request) {
        logger.error("An unexpected internal server error occurred. Request: {}", request.getDescription(false), ex);

        var errorResponse = new ErrorResponseResource(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected internal server error occurred.",
                request.getDescription(false),
                new Date()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}