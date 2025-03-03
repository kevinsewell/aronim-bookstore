package com.aronim.bookstore.security.test;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class WithMockJwtSecurityContextFactory implements WithSecurityContextFactory<WithMockJwt> {

    @Override
    public SecurityContext createSecurityContext(WithMockJwt annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "RS256");
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", annotation.subject());
        
        Map<String, Object> realmAccess = new HashMap<>();
        realmAccess.put("roles", annotation.roles());
        claims.put("realm_access", realmAccess);
        
        Jwt jwt = new Jwt("token", null, null, headers, claims);
        
        JwtAuthenticationToken authentication = new JwtAuthenticationToken(
            jwt, 
            Collections.emptyList(), 
            annotation.subject()
        );
        
        context.setAuthentication(authentication);
        return context;
    }
}
