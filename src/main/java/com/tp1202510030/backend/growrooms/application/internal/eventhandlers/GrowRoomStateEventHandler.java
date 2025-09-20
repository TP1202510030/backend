package com.tp1202510030.backend.growrooms.application.internal.eventhandlers;

import com.tp1202510030.backend.growrooms.domain.model.commands.growroom.ActivateGrowRoomCropCommand;
import com.tp1202510030.backend.growrooms.domain.model.commands.growroom.DeactivateGrowRoomCropCommand;
import com.tp1202510030.backend.growrooms.domain.model.events.CropCreatedEvent;
import com.tp1202510030.backend.growrooms.domain.model.events.CropFinishedEvent;
import com.tp1202510030.backend.growrooms.domain.services.growroom.GrowRoomCommandService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class GrowRoomStateEventHandler {

    private final GrowRoomCommandService growRoomCommandService;

    public GrowRoomStateEventHandler(GrowRoomCommandService growRoomCommandService) {
        this.growRoomCommandService = growRoomCommandService;
    }

    @EventListener
    public void on(CropCreatedEvent event) {
        var command = new ActivateGrowRoomCropCommand(event.getGrowRoomId());
        growRoomCommandService.handle(command);
    }

    @EventListener
    public void on(CropFinishedEvent event) {
        var command = new DeactivateGrowRoomCropCommand(event.getGrowRoomId());
        growRoomCommandService.handle(command);
    }
}