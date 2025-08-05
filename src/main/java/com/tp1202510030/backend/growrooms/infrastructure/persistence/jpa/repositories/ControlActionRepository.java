package com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories;

import com.tp1202510030.backend.growrooms.domain.model.entities.ControlAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ControlActionRepository extends JpaRepository<ControlAction, Long> {
    Page<ControlAction> findAllByCropPhaseId(Long cropPhaseId, Pageable pageable);

    @Query(value = "SELECT ca FROM ControlAction ca WHERE ca.id IN " +
            "(SELECT MAX(sub_ca.id) FROM ControlAction sub_ca WHERE sub_ca.cropPhase.id = :cropPhaseId GROUP BY sub_ca.actuatorType)",
            countQuery = "SELECT COUNT(ca) FROM ControlAction ca WHERE ca.id IN " +
                    "(SELECT MAX(sub_ca.id) FROM ControlAction sub_ca WHERE sub_ca.cropPhase.id = :cropPhaseId GROUP BY sub_ca.actuatorType)")
    Page<ControlAction> findLatestControlActionsByCropPhaseId(Long cropPhaseId, Pageable pageable);
}