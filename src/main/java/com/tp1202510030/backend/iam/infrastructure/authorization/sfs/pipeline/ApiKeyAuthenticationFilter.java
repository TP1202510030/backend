package com.tp1202510030.backend.iam.infrastructure.authorization.sfs.pipeline;

import com.tp1202510030.backend.iam.domain.model.valueobjects.Roles;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {
    private final String apiKeySecret;
    private static final String API_KEY_HEADER = "X-API-KEY";

    public ApiKeyAuthenticationFilter(String apiKeySecret) {
        this.apiKeySecret = apiKeySecret;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String receivedApiKey = request.getHeader(API_KEY_HEADER);

        if (Objects.nonNull(receivedApiKey) && receivedApiKey.equals(apiKeySecret)) {
            var authorities = Collections.singletonList(new SimpleGrantedAuthority(Roles.ROLE_LAMBDA.toString()));
            var authentication = new UsernamePasswordAuthenticationToken("lambda-service", null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
