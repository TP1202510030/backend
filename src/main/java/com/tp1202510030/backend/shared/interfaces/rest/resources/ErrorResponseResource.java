package com.tp1202510030.backend.shared.interfaces.rest.resources;

import java.util.Date;

public record ErrorResponseResource(
        int statusCode,
        String message,
        String description,
        Date timestamp
) {
}
