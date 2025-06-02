package com.tp1202510030.backend.growrooms.domain.services.crop;

import com.tp1202510030.backend.growrooms.domain.model.commands.crop.CreateCropPhaseCommand;

public interface CropPhaseCommandService {
    Long handle(CreateCropPhaseCommand command);
}
