package com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories;

import com.tp1202510030.backend.growrooms.domain.model.entities.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    List<Measurement> findAllByCropPhaseId(Long cropPhaseId);

    @Query("SELECT MAX(m.timestamp) FROM Measurement m WHERE m.cropPhase.id = :cropPhaseId")
    Optional<Date> findLatestMeasurementTimestamp(Long cropPhaseId);

    List<Measurement> findAllByCropPhaseIdAndTimestamp(Long cropPhaseId, Date timestamp);

    List<Measurement> findAllByCropPhaseIdOrderByTimestampDesc(Long cropPhaseId);
}
