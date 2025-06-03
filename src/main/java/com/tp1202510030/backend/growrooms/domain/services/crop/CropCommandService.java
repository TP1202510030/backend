package com.tp1202510030.backend.growrooms.domain.services.crop;

import com.tp1202510030.backend.growrooms.domain.model.commands.crop.AdvanceCropPhaseCommand;
import com.tp1202510030.backend.growrooms.domain.model.commands.crop.CreateCropCommand;

public interface CropCommandService {
    Long handle(CreateCropCommand command);

    void handle(AdvanceCropPhaseCommand command);
}
