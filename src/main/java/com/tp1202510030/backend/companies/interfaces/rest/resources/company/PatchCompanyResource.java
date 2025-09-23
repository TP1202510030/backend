package com.tp1202510030.backend.companies.interfaces.rest.resources.company;

import java.util.Optional;

public record PatchCompanyResource(
        Optional<String> companyName,
        Optional<Long> taxIdentificationNumber
) {
}
