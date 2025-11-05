package com.tp1202510030.backend.iam.application.internal.outboundservices.cookies;

import com.tp1202510030.backend.shared.infrastructure.authorization.SecurityConstants;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieServiceImpl implements CookieService {

    @Override
    public ResponseCookie createAuthCookie(String token) {
        return ResponseCookie.from(SecurityConstants.AUTH_COOKIE_NAME, token)
                .httpOnly(true)
                .secure(true)
                .path(SecurityConstants.COOKIE_PATH)
                .maxAge(SecurityConstants.COOKIE_MAX_AGE_DAYS * 24 * 60 * 60)
                .sameSite("None")
                .build();
    }

    @Override
    public ResponseCookie createInvalidationCookie() {
        return ResponseCookie.from(SecurityConstants.AUTH_COOKIE_NAME, null)
                .httpOnly(true)
                .secure(true)
                .path(SecurityConstants.COOKIE_PATH)
                .maxAge(0)
                .sameSite("None")
                .build();
    }
}
