package com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories;

import com.tp1202510030.backend.growrooms.domain.model.entities.ControlAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ControlActionRepository extends JpaRepository<ControlAction, Long> {
    List<ControlAction> findAllByCropPhaseId(Long cropPhaseId);

    @Query("SELECT ca FROM ControlAction ca WHERE ca.id IN " +
            "(SELECT MAX(sub_ca.id) FROM ControlAction sub_ca WHERE sub_ca.cropPhase.id = :cropPhaseId GROUP BY sub_ca.actuatorType)")
    List<ControlAction> findLatestControlActionsByCropPhaseId(Long cropPhaseId);
}
