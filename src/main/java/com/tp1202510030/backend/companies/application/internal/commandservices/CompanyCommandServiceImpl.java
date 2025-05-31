package com.tp1202510030.backend.companies.application.internal.commandservices;

import com.tp1202510030.backend.companies.domain.model.aggregates.Company;
import com.tp1202510030.backend.companies.domain.model.commands.company.CreateCompanyCommand;
import com.tp1202510030.backend.companies.domain.model.commands.company.UpdateCompanyCommand;
import com.tp1202510030.backend.companies.domain.model.valueobjects.CompanyName;
import com.tp1202510030.backend.companies.domain.model.valueobjects.TaxIdentificationNumber;
import com.tp1202510030.backend.companies.domain.services.company.CompanyCommandService;
import com.tp1202510030.backend.companies.infrastructure.persistence.jpa.repositories.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyCommandServiceImpl implements CompanyCommandService {
    private final CompanyRepository companyRepository;

    public CompanyCommandServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }


    @Override
    public Long handle(CreateCompanyCommand command) {
        var companyName = new CompanyName(command.name());
        var taxIdentificationNumber = new TaxIdentificationNumber(command.taxIdentificationNumber());

        companyRepository.findByName(companyName).map(company -> {
            throw new IllegalArgumentException("Company with name " + command.name() + " already exists");
        });

        companyRepository.findByTaxIdentificationNumber(taxIdentificationNumber).map(company -> {
            throw new IllegalArgumentException("Company with TIN " + command.taxIdentificationNumber() + " already exists");
        });

        var company = new Company(
                command.name(),
                command.taxIdentificationNumber()
        );
        companyRepository.save(company);
        return company.getId();
    }

    @Override
    public Optional<Company> handle(UpdateCompanyCommand command) {
        if (companyRepository.existsByNameAndIdIsNot(command.name(), command.companyId()))
            throw new IllegalArgumentException("Company with name %s already exists".formatted(command.name()));

        if (companyRepository.existsByTaxIdentificationNumberAndIdIsNot(command.taxIdentificationNumber(), command.companyId()))
            throw new IllegalArgumentException("Company with TIN %s already exists".formatted(command.taxIdentificationNumber()));


        var company = companyRepository.findById(command.companyId());
        if (company.isEmpty())
            throw new IllegalArgumentException("Company with id %s not found".formatted(command.companyId()));
        var companyToUpdate = company.get();
        try {
            var updatedCompany = companyRepository.save(companyToUpdate.updateInformation(
                    command.name(),
                    command.taxIdentificationNumber()
            ));
            return Optional.of(updatedCompany);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error updating company: %s".formatted(e.getMessage()));
        }
    }
}
