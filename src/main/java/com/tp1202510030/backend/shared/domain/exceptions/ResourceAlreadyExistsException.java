package com.tp1202510030.backend.shared.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceAlreadyExistsException extends RuntimeException {

    public ResourceAlreadyExistsException(String resourceName, String attribute, String value) {
        super("%s with %s '%s' already exists.".formatted(resourceName, attribute, value));
    }

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}