package com.tp1202510030.backend.iam.application.internal.outboundservices.cookies;

import com.tp1202510030.backend.shared.infrastructure.authorization.SecurityConstants;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

@Service
public class CookieServiceImpl implements CookieService {

    @Override
    public Cookie createAuthCookie(String token) {
        Cookie cookie = new Cookie(SecurityConstants.AUTH_COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath(SecurityConstants.COOKIE_PATH);
        cookie.setMaxAge(SecurityConstants.COOKIE_MAX_AGE_DAYS * 24 * 60 * 60);
        return cookie;
    }

    @Override
    public Cookie createInvalidationCookie() {
        Cookie cookie = new Cookie(SecurityConstants.AUTH_COOKIE_NAME, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath(SecurityConstants.COOKIE_PATH);
        cookie.setMaxAge(0);
        return cookie;
    }
}
