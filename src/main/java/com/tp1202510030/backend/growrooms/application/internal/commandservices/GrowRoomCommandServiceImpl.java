package com.tp1202510030.backend.growrooms.application.internal.commandservices;

import com.tp1202510030.backend.companies.infrastructure.persistence.jpa.repositories.CompanyRepository;
import com.tp1202510030.backend.growrooms.domain.model.aggregates.GrowRoom;
import com.tp1202510030.backend.growrooms.domain.model.commands.growroom.ActivateGrowRoomCropCommand;
import com.tp1202510030.backend.growrooms.domain.model.commands.growroom.CreateGrowRoomCommand;
import com.tp1202510030.backend.growrooms.domain.model.commands.growroom.DeactivateGrowRoomCropCommand;
import com.tp1202510030.backend.growrooms.domain.model.commands.growroom.UpdateGrowRoomCommand;
import com.tp1202510030.backend.growrooms.domain.model.valueobjects.GrowRoomName;
import com.tp1202510030.backend.growrooms.domain.services.growroom.GrowRoomCommandService;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.GrowRoomRepository;
import com.tp1202510030.backend.shared.domain.exceptions.ResourceAlreadyExistsException;
import com.tp1202510030.backend.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class GrowRoomCommandServiceImpl implements GrowRoomCommandService {
    private final GrowRoomRepository growRoomRepository;
    private final CompanyRepository companyRepository;

    public GrowRoomCommandServiceImpl(GrowRoomRepository growRoomRepository, CompanyRepository companyRepository) {
        this.growRoomRepository = growRoomRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    @Transactional
    public Long handle(CreateGrowRoomCommand command) {
        var company = companyRepository.findById(command.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company", "ID", command.companyId().toString()));


        GrowRoomName growRoomName = new GrowRoomName(command.name());

        growRoomRepository.findByName(growRoomName).ifPresent(g -> {
            throw new ResourceAlreadyExistsException("Grow room", "name", g.getGrowRoomName());
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
    @Transactional
    public Optional<GrowRoom> handle(UpdateGrowRoomCommand command) {
        var company = companyRepository.findById(command.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company", "ID", command.companyId().toString()));

        GrowRoomName growRoomName = new GrowRoomName(command.name());

        if (growRoomRepository.existsByNameAndIdIsNot(growRoomName, command.growRoomId())) {
            throw new ResourceAlreadyExistsException("Grow room", "name", command.name());
        }

        var growRoom = growRoomRepository.findById(command.growRoomId());
        if (growRoom.isEmpty()) {
            throw new ResourceNotFoundException("Grow room", "ID", command.growRoomId().toString());
        }

        var growRoomToUpdate = growRoom.get();

        var updatedGrowRoom = growRoomRepository.save(growRoomToUpdate.updateInformation(command.name(), command.imageUrl(), company));
        return Optional.of(updatedGrowRoom);
    }

    @Override
    @Transactional
    public void handle(ActivateGrowRoomCropCommand command) {
        var growRoom = growRoomRepository.findById(command.growRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Grow Room", "ID", command.growRoomId().toString()));
        growRoom.activateCrop();
        growRoomRepository.save(growRoom);
    }

    @Override
    @Transactional
    public void handle(DeactivateGrowRoomCropCommand command) {
        var growRoom = growRoomRepository.findById(command.growRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Grow Room", "ID", command.growRoomId().toString()));
        growRoom.deactivateCrop();
        growRoomRepository.save(growRoom);
    }
}

