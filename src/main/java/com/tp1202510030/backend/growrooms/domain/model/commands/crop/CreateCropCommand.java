package com.tp1202510030.backend.growrooms.domain.model.commands.crop;

import java.time.Duration;
import java.util.Date;
import java.util.List;

public record CreateCropCommand(
        Date startDate,
        Date endDate,
        Duration sensorActivationFrequency,
        Long growRoomId,
        List<CreateCropPhaseCommand> phases
) {
}
