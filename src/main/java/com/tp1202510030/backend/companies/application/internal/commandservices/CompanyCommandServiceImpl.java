package com.tp1202510030.backend.companies.application.internal.commandservices;

import com.tp1202510030.backend.companies.domain.model.aggregates.Company;
import com.tp1202510030.backend.companies.domain.model.commands.company.CreateCompanyCommand;
import com.tp1202510030.backend.companies.domain.model.commands.company.UpdateCompanyCommand;
import com.tp1202510030.backend.companies.domain.model.valueobjects.CompanyName;
import com.tp1202510030.backend.companies.domain.model.valueobjects.TaxIdentificationNumber;
import com.tp1202510030.backend.companies.domain.services.company.CompanyCommandService;
import com.tp1202510030.backend.companies.infrastructure.persistence.jpa.repositories.CompanyRepository;
import com.tp1202510030.backend.shared.domain.exceptions.ResourceAlreadyExistsException;
import com.tp1202510030.backend.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyCommandServiceImpl implements CompanyCommandService {
    private final CompanyRepository companyRepository;

    public CompanyCommandServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }


    @Override
    public Optional<Company> handle(CreateCompanyCommand command) {
        var companyName = new CompanyName(command.name());
        var taxIdentificationNumber = new TaxIdentificationNumber(command.taxIdentificationNumber());

        companyRepository.findByName(companyName).ifPresent(company -> {
            throw new ResourceAlreadyExistsException("Company", "name", command.name());
        });

        companyRepository.findByTaxIdentificationNumber(taxIdentificationNumber).ifPresent(company -> {
            throw new ResourceAlreadyExistsException("Company", "TIN", command.taxIdentificationNumber().toString());
        });

        var company = new Company(
                command.name(),
                command.taxIdentificationNumber()
        );
        companyRepository.save(company);
        return Optional.of(company);
    }

    @Override
    public Optional<Company> handle(UpdateCompanyCommand command) {
        if (companyRepository.existsByNameAndIdIsNot(command.name(), command.companyId()))
            throw new ResourceAlreadyExistsException("Company", "name", command.name().companyName());

        if (companyRepository.existsByTaxIdentificationNumberAndIdIsNot(command.taxIdentificationNumber(), command.companyId()))
            throw new ResourceAlreadyExistsException("Company", "TIN", command.taxIdentificationNumber().taxIdentificationNumber().toString());


        var companyToUpdate = companyRepository.findById(command.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company", "ID", command.companyId().toString()));

        try {
            var updatedCompany = companyRepository.save(companyToUpdate.updateInformation(
                    command.name(),
                    command.taxIdentificationNumber()
            ));
            return Optional.of(updatedCompany);
        } catch (Exception e) {
            throw new RuntimeException("Error updating company: %s".formatted(e.getMessage()));
        }
    }
}
