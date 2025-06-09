package com.tp1202510030.backend.growrooms.application.internal.commandservices;

import com.tp1202510030.backend.companies.domain.model.queries.company.GetCompanyByIdQuery;
import com.tp1202510030.backend.companies.domain.services.company.CompanyQueryService;
import com.tp1202510030.backend.growrooms.domain.model.aggregates.GrowRoom;
import com.tp1202510030.backend.growrooms.domain.model.commands.growroom.ActivateGrowRoomCropCommand;
import com.tp1202510030.backend.growrooms.domain.model.commands.growroom.CreateGrowRoomCommand;
import com.tp1202510030.backend.growrooms.domain.model.commands.growroom.DeactivateGrowRoomCropCommand;
import com.tp1202510030.backend.growrooms.domain.model.commands.growroom.UpdateGrowRoomCommand;
import com.tp1202510030.backend.growrooms.domain.model.valueobjects.GrowRoomName;
import com.tp1202510030.backend.growrooms.domain.services.growroom.GrowRoomCommandService;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.GrowRoomRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GrowRoomCommandServiceImpl implements GrowRoomCommandService {
    private final GrowRoomRepository growRoomRepository;
    private final CompanyQueryService companyQueryService;

    public GrowRoomCommandServiceImpl(GrowRoomRepository growRoomRepository, CompanyQueryService companyQueryService) {
        this.growRoomRepository = growRoomRepository;
        this.companyQueryService = companyQueryService;
    }

    @Override
    public Long handle(CreateGrowRoomCommand command) {
        var companyOpt = companyQueryService.handle(new GetCompanyByIdQuery(command.companyId()));
        if (companyOpt.isEmpty()) {
            throw new IllegalArgumentException("Company with ID " + command.companyId() + " not found");
        }
        var company = companyOpt.get();

        var growRoomName = new GrowRoomName(command.name());

        growRoomRepository.findByName(growRoomName).map(growRoom -> {
            throw new IllegalArgumentException("Grow room with name " + command.name() + " already exists");
        });

        var growRoom = new GrowRoom(
                command.name(),
                command.imageUrl(),
                company
        );
        growRoomRepository.save(growRoom);
        return growRoom.getId();
    }

    @Override
    public Optional<GrowRoom> handle(UpdateGrowRoomCommand command) {
        var companyOpt = companyQueryService.handle(new GetCompanyByIdQuery(command.companyId()));
        if (companyOpt.isEmpty()) {
            throw new IllegalArgumentException("Company with ID " + command.companyId() + " not found");
        }
        var company = companyOpt.get();

        if (growRoomRepository.existsByNameAndIdIsNot(command.name(), command.growRoomId())) {
            throw new IllegalArgumentException("Grow room with name %s already exists".formatted(command.name()));
        }

        var growRoom = growRoomRepository.findById(command.growRoomId());
        if (growRoom.isEmpty()) {
            throw new IllegalArgumentException("Grow room with id %s not found".formatted(command.growRoomId()));
        }

        var growRoomToUpdate = growRoom.get();
        try {
            var updatedGrowRoom = growRoomRepository.save(growRoomToUpdate.updateInformation(
                    command.name(),
                    command.imageUrl(),
                    company
            ));
            return Optional.of(updatedGrowRoom);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error updating grow room: %s".formatted(e.getMessage()));
        }
    }

    @Override
    public void handle(ActivateGrowRoomCropCommand command) {
        var growRoomOpt = growRoomRepository.findById(command.growRoomId());
        if (growRoomOpt.isEmpty()) {
            throw new IllegalArgumentException("Grow room with ID " + command.growRoomId() + " not found");
        }
        var growRoom = growRoomOpt.get();
        growRoom.activateCrop();
        growRoomRepository.save(growRoom);
    }

    @Override
    public void handle(DeactivateGrowRoomCropCommand command) {
        var growRoomOpt = growRoomRepository.findById(command.growRoomId());
        if (growRoomOpt.isEmpty()) {
            throw new IllegalArgumentException("Grow room with ID " + command.growRoomId() + " not found");
        }
        var growRoom = growRoomOpt.get();
        growRoom.deactivateCrop();
        growRoomRepository.save(growRoom);
    }
}

