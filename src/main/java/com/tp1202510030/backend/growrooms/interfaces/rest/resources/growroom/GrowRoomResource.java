package com.tp1202510030.backend.growrooms.interfaces.rest.resources.growroom;

import com.tp1202510030.backend.companies.domain.model.aggregates.Company;

public record GrowRoomResource(
        Long id,
        String name,
        String imageUrl,
        Company company
) {
}
