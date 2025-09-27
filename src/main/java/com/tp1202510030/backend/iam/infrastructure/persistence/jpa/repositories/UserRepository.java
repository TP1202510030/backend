package com.tp1202510030.backend.iam.infrastructure.persistence.jpa.repositories;

import com.tp1202510030.backend.iam.domain.model.aggregates.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    void deleteAllByCompanyId(Long companyId);

    Page<User> findAllByCompanyId(Long companyId, Pageable pageable);
}
