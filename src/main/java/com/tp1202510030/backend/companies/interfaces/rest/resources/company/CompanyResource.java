package com.tp1202510030.backend.companies.interfaces.rest.resources.company;

public record CompanyResource(
        Long id,
        String companyName,
        Long taxIdentificationNumber
) {
}
