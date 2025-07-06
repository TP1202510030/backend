package com.tp1202510030.backend.companies.interfaces.rest.resources.company;

import com.tp1202510030.backend.companies.domain.model.valueobjects.CompanyName;
import com.tp1202510030.backend.companies.domain.model.valueobjects.TaxIdentificationNumber;

public record UpdateCompanyResource(
        Long companyId,
        CompanyName companyName,
        TaxIdentificationNumber taxIdentificationNumber
) {
    public UpdateCompanyResource {
        if (companyId == null || companyId <= 0) {
            throw new IllegalArgumentException("companyId cannot be null or negative");
        }
        if (companyName == null || companyName.companyName().isBlank()) {
            throw new IllegalArgumentException("companyName cannot be null or empty");
        }
        if (taxIdentificationNumber == null) {
            throw new IllegalArgumentException("taxIdentificationNumber cannot be null or empty");
        }
    }
}
