package com.tp1202510030.backend.companies.domain.services.company;

import com.tp1202510030.backend.companies.domain.model.aggregates.Company;
import com.tp1202510030.backend.companies.domain.model.queries.company.GetAllCompaniesQuery;
import com.tp1202510030.backend.companies.domain.model.queries.company.GetCompanyByIdQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CompanyQueryService {
    Optional<Company> handle(GetCompanyByIdQuery query);

    Page<Company> handle(GetAllCompaniesQuery query, Pageable pageable);
}
