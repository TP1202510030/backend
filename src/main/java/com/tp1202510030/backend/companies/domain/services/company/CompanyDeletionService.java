package com.tp1202510030.backend.companies.domain.services.company;

import com.tp1202510030.backend.companies.domain.model.aggregates.Company;
import org.springframework.stereotype.Service;

@Service
public interface CompanyDeletionService {
    void deleteCompany(Company company);
}
