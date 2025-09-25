package com.tp1202510030.backend.companies.application.internal.services;

import com.tp1202510030.backend.companies.application.internal.outboundservices.acl.ExternalGrowRoomService;
import com.tp1202510030.backend.companies.application.internal.outboundservices.acl.ExternalIamService;
import com.tp1202510030.backend.companies.domain.model.aggregates.Company;
import com.tp1202510030.backend.companies.domain.services.company.CompanyDeletionService;
import com.tp1202510030.backend.companies.infrastructure.persistence.jpa.repositories.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyDeletionServiceImpl implements CompanyDeletionService {
    private final CompanyRepository companyRepository;
    private final ExternalIamService externalIamService;
    private final ExternalGrowRoomService externalGrowRoomService;

    public CompanyDeletionServiceImpl(
            CompanyRepository companyRepository,
            ExternalIamService externalIamService,
            ExternalGrowRoomService externalGrowRoomService
    ) {
        this.companyRepository = companyRepository;
        this.externalIamService = externalIamService;
        this.externalGrowRoomService = externalGrowRoomService;
    }

    /**
     * Deletes a company and all its associated data.
     *
     * @param company The company to be deleted.
     */
    @Override
    @Transactional
    public void deleteCompany(Company company) {
        externalIamService.deleteAllUsersFromCompany(company.getId());

        externalGrowRoomService.deleteAllGrowRoomsByCompanyId(company.getId());

        companyRepository.delete(company);
    }

}
