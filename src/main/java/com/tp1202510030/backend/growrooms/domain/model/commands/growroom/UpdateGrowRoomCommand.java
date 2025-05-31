package com.tp1202510030.backend.growrooms.domain.model.commands.growroom;

import com.tp1202510030.backend.companies.domain.model.aggregates.Company;
import com.tp1202510030.backend.growrooms.domain.model.valueobjects.GrowRoomName;

public record UpdateGrowRoomCommand(Long growRoomId, GrowRoomName name, String imageUrl, Long companyId) {
    public UpdateGrowRoomCommand {
        if (growRoomId == null) {
            throw new IllegalArgumentException("Grow room ID cannot be null");
        }
        if (name == null || name.name().isEmpty()) {
            throw new IllegalArgumentException("Grow room name cannot be null or empty");
        }
        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new IllegalArgumentException("Image URL cannot be null or empty");
        }
        if (companyId == null) {
            throw new IllegalArgumentException("Company ID cannot be null");
        }
    }
}
