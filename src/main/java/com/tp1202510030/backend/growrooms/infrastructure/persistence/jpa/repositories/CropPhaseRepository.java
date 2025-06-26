package com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories;

import com.tp1202510030.backend.growrooms.domain.model.entities.CropPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CropPhaseRepository extends JpaRepository<CropPhase, Long> {
}
