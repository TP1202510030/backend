package com.tp1202510030.backend.companies.application.internal.outboundservices.acl;

import com.tp1202510030.backend.iam.interfaces.acl.IamContextFacade;
import org.springframework.stereotype.Service;

@Service
public class ExternalIamService {
    private final IamContextFacade iamContextFacade;

    public ExternalIamService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    /**
     * Deletes all users from a company by the company ID
     *
     * @param companyId the ID of the company which the users to be deleted are part of
     */
    public void deleteAllUsersFromCompany(Long companyId) {
        iamContextFacade.deleteAllUsersByCompanyId(companyId);
    }
}
