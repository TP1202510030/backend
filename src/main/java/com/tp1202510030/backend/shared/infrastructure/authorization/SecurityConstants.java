package com.tp1202510030.backend.shared.infrastructure.authorization;

/**
 * Utility class to hold all security-related constants, especially SpEL expressions for @PreAuthorize annotations.
 * This helps avoid magic strings and centralizes security logic for better maintainability.
 */
public final class SecurityConstants {

    // --- Private constructor to prevent instantiation ---
    private SecurityConstants() {
    }

    // Webhook Headers
    public static final String SIGNATURE_HEADER = "x-signature";
    public static final String REQUEST_ID_HEADER = "x-request-id";

    // HMAC Signature parts
    public static final String SIGNATURE_TIMESTAMP_KEY = "ts";
    public static final String SIGNATURE_VERSION_KEY = "v1";

    // HMAC Algorithm
    public static final String HMAC_ALGORITHM = "HmacSHA256";

    // Redis Keys
    public static final String WEBHOOK_ID_PREFIX = "webhook_id:";
    public static final String WEBHOOK_PROCESSED_VALUE = "processed";
    public static final long WEBHOOK_ID_EXPIRATION_MINUTES = 5;

    // Cookie Configuration
    public static final String AUTH_COOKIE_NAME = "access_token";
    public static final String COOKIE_PATH = "/";
    public static final int COOKIE_MAX_AGE_DAYS = 7;

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
    public static final String IS_CROP_PHASE_OWNER = "@securityService.isCropPhaseOwner(authentication, #phaseId)";

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