package com.tp1202510030.backend.growrooms.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record GrowPhaseName(String name) {
    public GrowPhaseName() {
        this(null);
    }

    public GrowPhaseName {
        if (name == null) {
            throw new IllegalArgumentException("Grow phase name cannot be null or empty");
        }
    }
}
