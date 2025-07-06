package com.tp1202510030.backend.companies.infrastructure.persistence.jpa.repositories;

import com.tp1202510030.backend.companies.domain.model.aggregates.Company;
import com.tp1202510030.backend.companies.domain.model.valueobjects.CompanyName;
import com.tp1202510030.backend.companies.domain.model.valueobjects.TaxIdentificationNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByName(CompanyName name);
    Optional<Company> findByTaxIdentificationNumber(TaxIdentificationNumber taxIdentificationNumber);
    /**
     * Check if a Company with the given name exists and the ID is not the given ID
     *
     * @param name The name of the Company
     * @param id The ID of the Company
     * @return true if a Company with the given name exists and the ID is not the given ID, otherwise false
     */
    boolean existsByNameAndIdIsNot(CompanyName name, Long id);

    boolean existsByTaxIdentificationNumberAndIdIsNot(TaxIdentificationNumber taxIdentificationNumber, Long id);
}
