package com.tp1202510030.backend.iam.application.internal.eventhandlers;

import com.tp1202510030.backend.companies.domain.model.aggregates.Company;
import com.tp1202510030.backend.companies.domain.model.valueobjects.TaxIdentificationNumber;
import com.tp1202510030.backend.companies.infrastructure.persistence.jpa.repositories.CompanyRepository;
import com.tp1202510030.backend.iam.application.internal.outboundservices.hashing.HashingService;
import com.tp1202510030.backend.iam.domain.model.aggregates.User;
import com.tp1202510030.backend.iam.domain.model.commands.SeedRolesCommand;
import com.tp1202510030.backend.iam.domain.model.valueobjects.Roles;
import com.tp1202510030.backend.iam.domain.services.RoleCommandService;
import com.tp1202510030.backend.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.tp1202510030.backend.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Collections;

@Service
public class ApplicationReadyEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationReadyEventHandler.class);

    private final RoleCommandService roleCommandService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final HashingService hashingService;

    @Value("${app.security.superadmin.username}")
    private String superAdminUsername;

    @Value("${app.security.superadmin.password}")
    private String superAdminPassword;

    @Value("${app.security.superadmin.company-name}")
    private String superAdminCompanyName;

    @Value("${app.security.superadmin.company-tin}")
    private Long superAdminCompanyTin;

    public ApplicationReadyEventHandler(
            RoleCommandService roleCommandService,
            UserRepository userRepository,
            RoleRepository roleRepository,
            CompanyRepository companyRepository,
            HashingService hashingService
    ) {
        this.roleCommandService = roleCommandService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
        this.hashingService = hashingService;
    }

    @EventListener
    @Transactional
    public void on(ApplicationReadyEvent event) {
        var applicationName = event.getApplicationContext().getId();
        LOGGER.info("Starting to verify if roles seeding is needed for {} at {}", applicationName, currentTimestamp());

        var seedRolesCommand = new SeedRolesCommand();
        roleCommandService.handle(seedRolesCommand);
        LOGGER.info("Role seeding verification finished for {} at {}", applicationName, currentTimestamp());

        seedSuperAdmin();
        LOGGER.info("Data seeding verification finished for {} at {}", applicationName, currentTimestamp());
    }

    private Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    private void seedSuperAdmin() {
        if (!userRepository.existsByUsername(superAdminUsername)) {
            LOGGER.info("Super admin user not found. Creating...");

            // Ensure the System Master Company exists
            Company masterCompany = companyRepository.findByTaxIdentificationNumber(new TaxIdentificationNumber(superAdminCompanyTin))
                    .orElseGet(() -> {
                        LOGGER.info("Creating System Master Company...");
                        Company newCompany = new Company(superAdminCompanyName, superAdminCompanyTin);
                        return companyRepository.save(newCompany);
                    });

            // Ensure ROLE_ADMIN exists
            var adminRole = roleRepository.findByName(Roles.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("CRITICAL ERROR: ROLE_ADMIN not found. Role seeding might have failed."));

            // Create and save the super admin user
            var hashedPassword = hashingService.encode(superAdminPassword);
            var superAdmin = new User(superAdminUsername, hashedPassword, Collections.singletonList(adminRole), masterCompany);
            userRepository.save(superAdmin);
            LOGGER.info("Super admin user created successfully.");

        } else {
            LOGGER.info("Super admin user already exists. Skipping creation.");
        }
    }
}
