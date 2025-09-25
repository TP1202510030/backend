package com.tp1202510030.backend.growrooms.application.acl;

import com.tp1202510030.backend.growrooms.domain.model.commands.growroom.DeleteAllGrowRoomsByCompanyIdCommand;
import com.tp1202510030.backend.growrooms.domain.model.commands.growroom.DeleteGrowRoomCommand;
import com.tp1202510030.backend.growrooms.domain.services.growroom.GrowRoomCommandService;
import com.tp1202510030.backend.growrooms.interfaces.acl.GrowRoomContextFacade;
import org.springframework.stereotype.Service;

@Service
public class GrowRoomContextFacadeImpl implements GrowRoomContextFacade {
    private final GrowRoomCommandService growRoomCommandService;

    public GrowRoomContextFacadeImpl(GrowRoomCommandService growRoomCommandService) {
        this.growRoomCommandService = growRoomCommandService;
    }

    @Override
    public void deleteGrowRoomById(Long growRoomId) {
        if (growRoomId == null) {
            throw new IllegalArgumentException("Grow room ID cannot be null when deleting a grow room.");
        }

        var command = new DeleteGrowRoomCommand(growRoomId);
        growRoomCommandService.handle(command);
    }

    @Override
    public void deleteAllGrowRoomsByCompanyId(Long companyId) {
        if (companyId == null) {
            throw new IllegalArgumentException("Company ID cannot be null when deleting all Grow rooms.");
        }

        var command = new DeleteAllGrowRoomsByCompanyIdCommand(companyId);
        growRoomCommandService.handle(command);
    }
}
