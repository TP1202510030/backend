package com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories;

import com.tp1202510030.backend.growrooms.domain.model.entities.Measurement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    Page<Measurement> findAllByCropPhaseId(Long cropPhaseId, Pageable pageable);

    @Query("SELECT MAX(m.timestamp) FROM Measurement m WHERE m.cropPhase.id = :cropPhaseId")
    Optional<Date> findLatestMeasurementTimestamp(Long cropPhaseId);

    Page<Measurement> findAllByCropPhaseIdAndTimestamp(Long cropPhaseId, Date timestamp, Pageable pageable);

    Page<Measurement> findAllByCropPhaseIdOrderByTimestampDesc(Long cropPhaseId, Pageable pageable);
}