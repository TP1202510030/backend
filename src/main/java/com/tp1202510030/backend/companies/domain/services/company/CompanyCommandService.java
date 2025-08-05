package com.tp1202510030.backend.companies.domain.services.company;

import com.tp1202510030.backend.companies.domain.model.aggregates.Company;
import com.tp1202510030.backend.companies.domain.model.commands.company.CreateCompanyCommand;
import com.tp1202510030.backend.companies.domain.model.commands.company.UpdateCompanyCommand;

import java.util.Optional;

public interface CompanyCommandService {
    Optional<Company> handle(CreateCompanyCommand command);

    Optional<Company> handle(UpdateCompanyCommand command);
}
