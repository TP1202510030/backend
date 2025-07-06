package com.tp1202510030.backend.companies.interfaces.rest.transform.company;

import com.tp1202510030.backend.companies.domain.model.aggregates.Company;
import com.tp1202510030.backend.companies.interfaces.rest.resources.company.CompanyResource;

public class CompanyResourceFromEntityAssembler {
    public static CompanyResource toResourceFromEntity(Company entity){
        return new CompanyResource(
                entity.getId(),
                entity.getCompanyName(),
                entity.getTaxIdentificationNumber()
        );
    }
}
