package com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.GrowRoom;
import com.tp1202510030.backend.growrooms.domain.model.valueobjects.GrowRoomName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrowRoomRepository extends JpaRepository<GrowRoom, Long> {
    boolean existsByNameAndCompanyId(GrowRoomName name, Long companyId);

    List<GrowRoom> findAllByCompanyId(Long companyId);

    boolean existsByNameAndIdIsNot(GrowRoomName name, Long id);
}
