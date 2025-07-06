package com.tp1202510030.backend.companies.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record CompanyName(String companyName) {
    public CompanyName() {
        this(null);
    }

    public CompanyName {
        if (companyName == null) {
            throw new IllegalArgumentException("Company name cannot be null or empty");
        }
    }

}
