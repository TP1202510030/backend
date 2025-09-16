package com.tp1202510030.backend.companies.interfaces.rest.resources.company;

public record CreateCompanyResource(
        String companyName,
        Long taxIdentificationNumber
) {
    public CreateCompanyResource {
        if (companyName == null || companyName.isBlank()) {
            throw new IllegalArgumentException("companyName cannot be null or empty");
        }
        if (taxIdentificationNumber == null) {
            throw new IllegalArgumentException("taxIdentificationNumber cannot be null or empty");
        }
        if (taxIdentificationNumber.toString().length() != 11) {
            throw new IllegalArgumentException("taxIdentificationNumber must be 11 digits");
        }
    }
}
