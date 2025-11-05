package com.tp1202510030.backend.iam.application.internal.outboundservices.cookies;

import org.springframework.http.ResponseCookie;

public interface CookieService {
    ResponseCookie createAuthCookie(String token);

    ResponseCookie createInvalidationCookie();
}
