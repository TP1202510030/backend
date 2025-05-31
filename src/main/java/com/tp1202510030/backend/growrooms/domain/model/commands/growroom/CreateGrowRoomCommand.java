package com.tp1202510030.backend.growrooms.domain.model.commands.growroom;

public record CreateGrowRoomCommand(String name, String imageUrl, Long companyId) {
}