package com.tp1202510030.backend.iam.application.internal.outboundservices.cookies;

import jakarta.servlet.http.Cookie;

public interface CookieService {
    Cookie createAuthCookie(String token);

    Cookie createInvalidationCookie();
}
