package com.tp1202510030.backend.companies.interfaces.rest.resources.company;

public record CreateCompanyResource(
        String companyName,
        Integer taxIdentificationNumber
) {
}
