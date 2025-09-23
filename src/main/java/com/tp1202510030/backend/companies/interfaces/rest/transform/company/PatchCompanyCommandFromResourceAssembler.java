package com.tp1202510030.backend.companies.interfaces.rest.transform.company;

import com.tp1202510030.backend.companies.domain.model.commands.company.PatchCompanyCommand;
import com.tp1202510030.backend.companies.interfaces.rest.resources.company.PatchCompanyResource;

public class PatchCompanyCommandFromResourceAssembler {
    public static PatchCompanyCommand toCommandFromResource(Long companyId, PatchCompanyResource resource) {
        return new PatchCompanyCommand(
                companyId,
                resource.companyName(),
                resource.taxIdentificationNumber()
        );
    }
}
