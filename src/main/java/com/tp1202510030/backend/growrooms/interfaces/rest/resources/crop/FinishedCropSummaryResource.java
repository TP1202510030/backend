package com.tp1202510030.backend.growrooms.interfaces.rest.resources.crop;

import java.util.Date;

public record FinishedCropSummaryResource(
        Long id,
        Date startDate,
        Date endDate,
        Double totalProduction,
        Long growRoomId
) {
}
