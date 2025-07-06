package com.tp1202510030.backend.companies.interfaces.rest.transform.company;

import com.tp1202510030.backend.companies.domain.model.commands.company.CreateCompanyCommand;
import com.tp1202510030.backend.companies.interfaces.rest.resources.company.CreateCompanyResource;

public class CreateCompanyCommandFromResourceAssembler {
    public static CreateCompanyCommand toCommandFromResource(CreateCompanyResource resource) {
        return new CreateCompanyCommand(
                resource.companyName(),
                resource.taxIdentificationNumber()
        );
    }
}
