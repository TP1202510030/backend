package com.tp1202510030.backend.companies.domain.services.company;

import com.tp1202510030.backend.companies.domain.model.aggregates.Company;
import com.tp1202510030.backend.companies.domain.model.queries.company.GetCompanyByIdQuery;

import java.util.Optional;

public interface CompanyQueryService {
    Optional<Company> handle(GetCompanyByIdQuery query);
}
