package com.tp1202510030.backend.growrooms.application.internal.commandservices;

import com.tp1202510030.backend.growrooms.domain.model.commands.cropphase.CreateCropPhaseCommand;
import com.tp1202510030.backend.growrooms.domain.services.cropphase.CropPhaseCommandService;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.CropPhaseRepository;
import org.springframework.stereotype.Service;

@Service
public class CropPhaseCommandServiceImpl implements CropPhaseCommandService {
    private final CropPhaseRepository cropPhaseRepository;

    CropPhaseCommandServiceImpl(CropPhaseRepository cropPhaseRepository) {
        this.cropPhaseRepository = cropPhaseRepository;
    }

    @Override
    public Long handle(CreateCropPhaseCommand command) {
        return 0L;
    }
}
