package com.tp1202510030.backend.growrooms.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record GrowRoomName(String name) {
    public GrowRoomName() {
        this(null);
    }

    public GrowRoomName {
        if (name == null) {
            throw new IllegalArgumentException("Grow room name cannot be null or empty");
        }
    }
}
