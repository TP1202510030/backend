package com.tp1202510030.backend.companies.domain.model.commands.company;

import com.tp1202510030.backend.companies.domain.model.valueobjects.CompanyName;
import com.tp1202510030.backend.companies.domain.model.valueobjects.TaxIdentificationNumber;

public record UpdateCompanyCommand(Long companyId, CompanyName name, TaxIdentificationNumber taxIdentificationNumber) {
    public UpdateCompanyCommand {
        if (name == null || name.companyName().isBlank()) {
            throw new IllegalArgumentException("Company name cannot be null or empty");
        }
        if (taxIdentificationNumber == null || taxIdentificationNumber.taxIdentificationNumber() < 0) {
            throw new IllegalArgumentException("Tax identification number cannot be negative");
        }
    }
}
