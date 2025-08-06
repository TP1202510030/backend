package com.tp1202510030.backend.iam.domain.model.valueobjects;

/**
 * Defines the roles available in the system.
 * ROLE_ADMIN: Superuser, can manage the entire system.
 * ROLE_COMPANY_ADMIN: Manages a specific company, its users, and its resources.
 * ROLE_COMPANY_TECHNICIAN: Operative user, can view resources of their company.
 */
public enum Roles {
    ROLE_ADMIN,
    ROLE_COMPANY_ADMIN,
    ROLE_COMPANY_TECHNICIAN,
    ROLE_LAMBDA
}
