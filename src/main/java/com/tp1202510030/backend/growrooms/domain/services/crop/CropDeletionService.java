package com.tp1202510030.backend.growrooms.domain.services.crop;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.Crop;

public interface CropDeletionService {
    void deleteCropAndAssociations(Crop crop);
}
