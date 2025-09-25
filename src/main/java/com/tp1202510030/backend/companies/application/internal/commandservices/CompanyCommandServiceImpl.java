package com.tp1202510030.backend.companies.application.internal.commandservices;

import com.tp1202510030.backend.companies.domain.model.aggregates.Company;
import com.tp1202510030.backend.companies.domain.model.commands.company.CreateCompanyCommand;
import com.tp1202510030.backend.companies.domain.model.commands.company.DeleteCompanyCommand;
import com.tp1202510030.backend.companies.domain.model.commands.company.PatchCompanyCommand;
import com.tp1202510030.backend.companies.domain.model.commands.company.UpdateCompanyCommand;
import com.tp1202510030.backend.companies.domain.model.valueobjects.CompanyName;
import com.tp1202510030.backend.companies.domain.model.valueobjects.TaxIdentificationNumber;
import com.tp1202510030.backend.companies.domain.services.company.CompanyCommandService;
import com.tp1202510030.backend.companies.domain.services.company.CompanyDeletionService;
import com.tp1202510030.backend.companies.infrastructure.persistence.jpa.repositories.CompanyRepository;
import com.tp1202510030.backend.shared.domain.exceptions.ResourceAlreadyExistsException;
import com.tp1202510030.backend.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyCommandServiceImpl implements CompanyCommandService {
    private final CompanyRepository companyRepository;
    private final CompanyDeletionService companyDeletionService;

    public CompanyCommandServiceImpl(
            CompanyRepository companyRepository,
            CompanyDeletionService companyDeletionService
    ) {
        this.companyRepository = companyRepository;
        this.companyDeletionService = companyDeletionService;
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
        CompanyName newName = new CompanyName(command.name());
        if (companyRepository.existsByNameAndIdIsNot(newName, command.companyId()))
            throw new ResourceAlreadyExistsException("Company", "name", command.name());

        TaxIdentificationNumber newTaxIdentificationNumber = new TaxIdentificationNumber(command.taxIdentificationNumber());
        if (companyRepository.existsByTaxIdentificationNumberAndIdIsNot(newTaxIdentificationNumber, command.companyId()))
            throw new ResourceAlreadyExistsException("Company", "TIN", command.taxIdentificationNumber().toString());


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

    @Override
    public Optional<Company> handle(PatchCompanyCommand command) {
        var companyToPatch = companyRepository.findById(command.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company", "ID", command.companyId().toString()));

        command.name().ifPresent(name -> {
            CompanyName newName = new CompanyName(name);
            if (companyRepository.existsByNameAndIdIsNot(newName, command.companyId()))
                throw new ResourceAlreadyExistsException("Company", "name", name);
        });

        command.taxIdentificationNumber().ifPresent(tin -> {
            TaxIdentificationNumber newTaxIdentificationNumber = new TaxIdentificationNumber(tin);
            if (companyRepository.existsByTaxIdentificationNumberAndIdIsNot(newTaxIdentificationNumber, command.companyId()))
                throw new ResourceAlreadyExistsException("Company", "TIN", tin.toString());
        });

        try {
            var patchedCompany = companyRepository.save(companyToPatch.updateInformation(
                    command.name().orElse(companyToPatch.getCompanyName()),
                    command.taxIdentificationNumber().orElse(companyToPatch.getTaxIdentificationNumber())
            ));
            return Optional.of(patchedCompany);
        } catch (Exception e) {
            throw new RuntimeException("Error patching company: %s".formatted(e.getMessage()));
        }
    }

    @Override
    public void handle(DeleteCompanyCommand command) {
        var companyToDelete = companyRepository.findById(command.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company", "ID", command.companyId().toString()));

        companyDeletionService.deleteCompany(companyToDelete);
    }
}
