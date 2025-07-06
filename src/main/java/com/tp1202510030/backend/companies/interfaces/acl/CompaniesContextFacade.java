package com.tp1202510030.backend.companies.interfaces.acl;

import com.tp1202510030.backend.companies.domain.model.aggregates.Company;

import java.util.Optional;

public interface CompaniesContextFacade {
    Optional<Company> findCompanyById(Long companyId);
}
