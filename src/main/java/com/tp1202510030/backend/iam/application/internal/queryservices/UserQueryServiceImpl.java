package com.tp1202510030.backend.iam.application.internal.queryservices;

import com.tp1202510030.backend.iam.application.internal.outboundservices.acl.IamExternalCompanyService;
import com.tp1202510030.backend.iam.domain.model.aggregates.User;
import com.tp1202510030.backend.iam.domain.model.queries.GetAllUsersByCompanyIdQuery;
import com.tp1202510030.backend.iam.domain.model.queries.GetAllUsersQuery;
import com.tp1202510030.backend.iam.domain.model.queries.GetUserByIdQuery;
import com.tp1202510030.backend.iam.domain.model.queries.GetUserByUsernameQuery;
import com.tp1202510030.backend.iam.domain.services.user.UserQueryService;
import com.tp1202510030.backend.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.tp1202510030.backend.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;
    private final IamExternalCompanyService externalCompanyService;

    public UserQueryServiceImpl(
            UserRepository userRepository,
            IamExternalCompanyService externalCompanyService
    ) {
        this.userRepository = userRepository;
        this.externalCompanyService = externalCompanyService;
    }

    @Override
    public List<User> handle(GetAllUsersQuery query) {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> handle(GetUserByIdQuery query) {
        return userRepository.findById(query.userId());
    }

    @Override
    public Optional<User> handle(GetUserByUsernameQuery query) {
        return userRepository.findByUsername(query.username());
    }

    @Override
    public Optional<Iterable<User>> handle(GetAllUsersByCompanyIdQuery query) {
        var company = externalCompanyService.getCompanyById(query.companyId());

        if (company.isEmpty()) {
            throw new ResourceNotFoundException("Company", "ID", query.companyId().toString());
        }

        return userRepository.findAllByCompanyId(company.get().getId());
    }
}
