package com.tp1202510030.backend.growrooms.application.internal.commandservices;

import com.tp1202510030.backend.companies.infrastructure.persistence.jpa.repositories.CompanyRepository;
import com.tp1202510030.backend.growrooms.domain.model.aggregates.Crop;
import com.tp1202510030.backend.growrooms.domain.model.aggregates.GrowRoom;
import com.tp1202510030.backend.growrooms.domain.model.commands.growroom.*;
import com.tp1202510030.backend.growrooms.domain.model.valueobjects.DeviceCredentials;
import com.tp1202510030.backend.growrooms.domain.model.valueobjects.GrowRoomName;
import com.tp1202510030.backend.growrooms.domain.services.crop.CropDeletionService;
import com.tp1202510030.backend.growrooms.domain.services.growroom.GrowRoomCommandService;
import com.tp1202510030.backend.growrooms.domain.services.iot.IotDeviceProvisioningService;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.CropRepository;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.GrowRoomRepository;
import com.tp1202510030.backend.shared.domain.exceptions.ResourceAlreadyExistsException;
import com.tp1202510030.backend.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GrowRoomCommandServiceImpl implements GrowRoomCommandService {
    private final GrowRoomRepository growRoomRepository;
    private final CompanyRepository companyRepository;
    private final IotDeviceProvisioningService iotProvisioningService;
    private final CropRepository cropRepository;
    private final CropDeletionService cropDeletionService;

    public GrowRoomCommandServiceImpl(GrowRoomRepository growRoomRepository,
                                      CompanyRepository companyRepository,
                                      IotDeviceProvisioningService iotProvisioningService,
                                      CropDeletionService cropDeletionService,
                                      CropRepository cropRepository) {
        this.growRoomRepository = growRoomRepository;
        this.companyRepository = companyRepository;
        this.iotProvisioningService = iotProvisioningService;
        this.cropRepository = cropRepository;
        this.cropDeletionService = cropDeletionService;
    }

    @Override
    @Transactional
    public Optional<DeviceCredentials> handle(CreateGrowRoomCommand command) {
        var company = companyRepository.findById(command.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company", "ID", command.companyId().toString()));


        GrowRoomName growRoomName = new GrowRoomName(command.name());

        if (growRoomRepository.existsByNameAndCompanyId(growRoomName, command.companyId())) {
            throw new ResourceAlreadyExistsException("Grow room", "name", command.name());
        }

        var growRoom = new GrowRoom(
                command.name(),
                command.imageUrl(),
                company
        );
        growRoomRepository.save(growRoom);
        return iotProvisioningService.provisionDevice(company.getId(), growRoom.getId());
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
    public Optional<GrowRoom> handle(PatchGrowRoomCommand command) {
        var growRoomToUpdate = growRoomRepository.findById(command.growRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("GrowRoom", "ID", command.growRoomId().toString()));

        command.name().ifPresent(name -> {
            GrowRoomName newName = new GrowRoomName(name);
            if (growRoomRepository.existsByNameAndIdIsNot(newName, command.growRoomId())) {
                throw new ResourceAlreadyExistsException("Grow room", "name", name);
            }
            growRoomToUpdate.changeName(newName);
        });

        command.imageUrl().ifPresent(growRoomToUpdate::changeImageUrl);

        // Para este ejemplo, actualizaremos directamente, pero lo ideal es usar métodos en el Agregado
        var updatedGrowRoom = growRoomToUpdate.updateInformation(
                command.name().orElse(growRoomToUpdate.getGrowRoomName()),
                command.imageUrl().orElse(growRoomToUpdate.getImageUrl()),
                growRoomToUpdate.getCompany()
        );


        return Optional.of(growRoomRepository.save(updatedGrowRoom));
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

    @Override
    @Transactional
    public void handle(DeleteGrowRoomCommand command) {
        GrowRoom growRoom = growRoomRepository.findById(command.growRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("GrowRoom", "ID", command.growRoomId().toString()));

        if (growRoom.getHasActiveCrop()) {
            throw new IllegalStateException("Cannot delete a grow room with an active crop.");
        }

        List<Crop> cropsToDelete = cropRepository.findAllByGrowRoomId(growRoom.getId(), Pageable.unpaged()).getContent();
        for (Crop crop : cropsToDelete) {
            cropDeletionService.deleteCropAndAssociations(crop);
        }

        iotProvisioningService.deprovisionDevice(growRoom.getCompany().getId(), growRoom.getId());
        growRoomRepository.delete(growRoom);
    }
}

