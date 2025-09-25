package com.tp1202510030.backend.growrooms.domain.services.growroom;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.GrowRoom;
import org.springframework.stereotype.Service;

@Service
public interface GrowRoomDeletionService {
    void deleteGrowRoomAndAssociations(GrowRoom growRoom);
}
