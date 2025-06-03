package com.tp1202510030.backend.growrooms.domain.services.cropphase;

import com.tp1202510030.backend.growrooms.domain.model.commands.cropphase.CreateCropPhaseCommand;

public interface CropPhaseCommandService {
    Long handle(CreateCropPhaseCommand command);
}
