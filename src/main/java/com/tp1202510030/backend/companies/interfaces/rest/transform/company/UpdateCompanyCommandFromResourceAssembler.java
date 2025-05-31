package com.tp1202510030.backend.companies.interfaces.rest.transform.company;

import com.tp1202510030.backend.companies.domain.model.commands.company.UpdateCompanyCommand;
import com.tp1202510030.backend.companies.interfaces.rest.resources.company.UpdateCompanyResource;

public class UpdateCompanyCommandFromResourceAssembler {
    public static UpdateCompanyCommand toCommandFromResource(Long companyId, UpdateCompanyResource resource) {
        return new UpdateCompanyCommand(
                companyId,
                resource.companyName(),
                resource.taxIdentificationNumber()
        );
    }
}
