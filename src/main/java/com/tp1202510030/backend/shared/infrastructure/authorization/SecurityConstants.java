package com.tp1202510030.backend.shared.infrastructure.authorization;

/**
 * Utility class to hold all security-related constants, especially SpEL expressions for @PreAuthorize annotations.
 * This helps avoid magic strings and centralizes security logic for better maintainability.
 */
public final class SecurityConstants {

    // --- Private constructor to prevent instantiation ---
    private SecurityConstants() {
    }

    // --- Role-Based Permissions ---
    public static final String IS_ADMIN = "hasRole('ADMIN')";
    public static final String IS_COMPANY_ADMIN = "hasRole('COMPANY_ADMIN')";
    public static final String IS_COMPANY_TECHNICIAN = "hasRole('COMPANY_TECHNICIAN')";
    public static final String IS_LAMBDA = "hasRole('LAMBDA')";

    // --- Ownership-Based Permissions ---
    public static final String IS_USER_SELF = "#userId == authentication.principal.id";
    public static final String IS_COMPANY_OWNER = "@securityService.isResourceOwner(authentication, #companyId)";
    public static final String IS_GROW_ROOM_OWNER = "@securityService.isGrowRoomOwner(authentication, #growRoomId)";
    public static final String IS_CROP_OWNER = "@securityService.isCropOwner(authentication, #cropId)";
    public static final String IS_CROP_PHASE_OWNER = "@securityService.isCropPhaseOwner(authentication, #cropPhaseId)";

    // --- Combined Permissions ---
    public static final String ADMIN_OR_USER_SELF = IS_ADMIN + " or " + IS_USER_SELF;
    public static final String ADMIN_OR_COMPANY_OWNER = IS_ADMIN + " or " + IS_COMPANY_OWNER;
    public static final String ADMIN_OR_GROW_ROOM_OWNER = IS_ADMIN + " or " + IS_GROW_ROOM_OWNER;
    public static final String ADMIN_OR_CROP_OWNER = IS_ADMIN + " or " + IS_CROP_OWNER;
    public static final String ADMIN_OR_CROP_PHASE_OWNER = IS_ADMIN + " or " + IS_CROP_PHASE_OWNER;
    public static final String ADMIN_OR_LAMBDA = IS_ADMIN + " or " + IS_LAMBDA;

    // --- Company Admin Permissions ---
    public static final String COMPANY_ADMIN_OR_HIGHER_AND_OWNER = IS_ADMIN + " or (" + IS_COMPANY_ADMIN + " and " + IS_COMPANY_OWNER + ")";

}