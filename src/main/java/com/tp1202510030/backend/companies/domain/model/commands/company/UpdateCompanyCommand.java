package com.tp1202510030.backend.companies.domain.model.commands.company;

public record UpdateCompanyCommand(Long companyId, String name, Long taxIdentificationNumber) {
    public UpdateCompanyCommand {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Company name cannot be null or empty");
        }
        if (taxIdentificationNumber == null || taxIdentificationNumber < 0) {
            throw new IllegalArgumentException("Tax identification number cannot be negative");
        }
    }
}
