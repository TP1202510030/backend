package com.tp1202510030.backend.iam.application.internal.outboundservices.acl;

import com.tp1202510030.backend.companies.domain.model.aggregates.Company;
import com.tp1202510030.backend.companies.interfaces.acl.CompaniesContextFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IamExternalCompanyService {
    private final CompaniesContextFacade companiesContextFacade;

    public IamExternalCompanyService(CompaniesContextFacade companiesContextFacade) {
        this.companiesContextFacade = companiesContextFacade;
    }

    public Optional<Company> getCompanyById(Long id) {
        return companiesContextFacade.findCompanyById(id);
    }
}