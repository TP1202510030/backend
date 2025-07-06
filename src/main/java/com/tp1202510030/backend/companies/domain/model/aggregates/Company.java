package com.tp1202510030.backend.companies.domain.model.aggregates;

import com.tp1202510030.backend.companies.domain.model.valueobjects.CompanyName;
import com.tp1202510030.backend.companies.domain.model.valueobjects.TaxIdentificationNumber;
import com.tp1202510030.backend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
public class Company extends AuditableAbstractAggregateRoot<Company> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Embedded
    private CompanyName name;

    @Embedded
    private TaxIdentificationNumber taxIdentificationNumber;

    public Company() {
    }

    public Company(String name, Long taxIdentificationNumber) {
        this.name = new CompanyName(name);
        this.taxIdentificationNumber = new TaxIdentificationNumber(taxIdentificationNumber);
    }

    public String getCompanyName() {
        return name.companyName();
    }

    public Long getTaxIdentificationNumber() {
        return taxIdentificationNumber.taxIdentificationNumber();
    }

    /**
     * Update company information.
     *
     * @param name                    Company name.
     * @param taxIdentificationNumber Company gender.
     * @return Company instance.
     */
    public Company updateInformation(CompanyName name, TaxIdentificationNumber taxIdentificationNumber) {
        this.name = name;
        this.taxIdentificationNumber = taxIdentificationNumber;
        return this;
    }
}
