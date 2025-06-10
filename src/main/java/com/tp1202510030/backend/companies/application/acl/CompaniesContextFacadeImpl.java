package com.tp1202510030.backend.companies.application.acl;

import com.tp1202510030.backend.companies.domain.model.aggregates.Company;
import com.tp1202510030.backend.companies.infrastructure.persistence.jpa.repositories.CompanyRepository;
import com.tp1202510030.backend.companies.interfaces.acl.CompaniesContextFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompaniesContextFacadeImpl implements CompaniesContextFacade {
    private final CompanyRepository companyRepository;

    public CompaniesContextFacadeImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Optional<Company> findCompanyById(Long companyId) {
        return companyRepository.findById(companyId);
    }
}
