package com.tp1202510030.backend.growrooms.application.internal.services;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.Crop;
import com.tp1202510030.backend.growrooms.domain.model.aggregates.GrowRoom;
import com.tp1202510030.backend.growrooms.domain.services.crop.CropDeletionService;
import com.tp1202510030.backend.growrooms.domain.services.growroom.GrowRoomDeletionService;
import com.tp1202510030.backend.growrooms.domain.services.iot.IotDeviceProvisioningService;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.CropRepository;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.GrowRoomRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrowRoomDeletionServiceImpl implements GrowRoomDeletionService {
    private final CropRepository cropRepository;
    private final GrowRoomRepository growRoomRepository;
    private final CropDeletionService cropDeletionService;
    private final IotDeviceProvisioningService iotDeviceProvisioningService;

    public GrowRoomDeletionServiceImpl(
            CropRepository cropRepository,
            GrowRoomRepository growRoomRepository,
            CropDeletionService cropDeletionService,
            IotDeviceProvisioningService iotDeviceProvisioningService
    ) {
        this.cropRepository = cropRepository;
        this.growRoomRepository = growRoomRepository;
        this.cropDeletionService = cropDeletionService;
        this.iotDeviceProvisioningService = iotDeviceProvisioningService;
    }

    public void deleteGrowRoomAndAssociations(GrowRoom growRoom) {
        List<Crop> cropsToDelete = cropRepository.findAllByGrowRoomId(growRoom.getId(), Pageable.unpaged()).getContent();
        for (Crop crop : cropsToDelete) {
            cropDeletionService.deleteCropAndAssociations(crop);
        }

        iotDeviceProvisioningService.deprovisionDevice(growRoom.getCompany().getId(), growRoom.getId());
        growRoomRepository.delete(growRoom);
    }
}
