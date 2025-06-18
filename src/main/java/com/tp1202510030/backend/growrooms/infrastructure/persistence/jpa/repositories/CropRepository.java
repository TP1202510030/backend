package com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.Crop;
import com.tp1202510030.backend.growrooms.domain.model.entities.CropPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CropRepository extends JpaRepository<Crop, Long> {
    List<Crop> findByEndDateIsNotNull();

    List<Crop> findAllByGrowRoomId(Long growRoomId);

    Optional<Crop> findFirstByGrowRoomIdAndEndDateIsNull(Long growRoomId);

    default CropPhase findActivePhaseByCropIdOrThrow(Long cropId) {
        Crop crop = findById(cropId)
                .orElseThrow(() -> new IllegalArgumentException("Crop not found with id " + cropId));

        CropPhase currentPhase = crop.getCurrentPhase();
        if (currentPhase == null) {
            throw new IllegalStateException("Crop with id " + cropId + " does not have a current phase set");
        }
        return currentPhase;
    }
}
