package com.tp1202510030.backend.companies.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record TaxIdentificationNumber(Integer taxIdentificationNumber) {
    public TaxIdentificationNumber() {
        this(null);
    }

    public TaxIdentificationNumber {
        if (taxIdentificationNumber == null) {
            throw new IllegalArgumentException("Tax identification number cannot be null or empty");
        }
    }

}
