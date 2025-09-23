package com.tp1202510030.backend.companies.interfaces.rest.resources.company;

public record UpdateCompanyResource(
        Long companyId,
        String companyName,
        Long taxIdentificationNumber
) {
    public UpdateCompanyResource {
        if (companyId == null || companyId <= 0) {
            throw new IllegalArgumentException("companyId cannot be null or negative");
        }
        if (companyName == null || companyName.isBlank()) {
            throw new IllegalArgumentException("companyName cannot be null or empty");
        }
        if (taxIdentificationNumber == null) {
            throw new IllegalArgumentException("taxIdentificationNumber cannot be null or empty");
        }
    }
}
