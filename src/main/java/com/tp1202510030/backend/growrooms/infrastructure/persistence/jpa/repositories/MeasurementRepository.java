package com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories;

import com.tp1202510030.backend.growrooms.domain.model.entities.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    List<Measurement> findAllByCropPhaseId(Long cropPhaseId);

    List<Measurement> findAllByCropPhaseIdOrderByTimestampDesc(Long cropPhaseId);
}
