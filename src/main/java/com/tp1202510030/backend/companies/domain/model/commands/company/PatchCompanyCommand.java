package com.tp1202510030.backend.companies.domain.model.commands.company;

import java.util.Optional;

public record PatchCompanyCommand(
        Long companyId,
        Optional<String> name,
        Optional<Long> taxIdentificationNumber
) {
    public PatchCompanyCommand {

    }
}
