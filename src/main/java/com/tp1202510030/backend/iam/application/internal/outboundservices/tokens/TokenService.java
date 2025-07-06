package com.tp1202510030.backend.iam.application.internal.outboundservices.tokens;

public interface TokenService {
    String generateToken(String username);

    String getUsernameFromToken(String token);

    boolean validateToken(String token);
}
