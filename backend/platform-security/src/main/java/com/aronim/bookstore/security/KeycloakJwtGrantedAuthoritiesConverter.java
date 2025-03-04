package com.aronim.bookstore.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class KeycloakJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

  /**
   * Prefix used for realm level roles.
   */
  public static final String PREFIX_ROLE = "ROLE_";

  /**
   * Name of the claim containing the realm level roles
   */
  private static final String CLAIM_REALM_ACCESS = "realm_access";

  /**
   * Name of the claim containing roles. (Applicable to realm and resource level.)
   */
  private static final String CLAIM_ROLES = "roles";

  /**
   * Extracts the realm and resource level roles from a JWT token distinguishing between them using prefixes.
   */
  @Override
  public Collection<GrantedAuthority> convert(final Jwt jwt) {

    // Collection that will hold the extracted roles
    final Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

    // Realm roles
    // Get the part of the access token that holds the roles assigned on realm level
    final Map<String, Collection<String>> realmAccess = jwt.getClaim(CLAIM_REALM_ACCESS);

    // Verify that the claim exists and is not empty
    if (realmAccess != null && !realmAccess.isEmpty()) {

      // From the realm_access claim get the roles
      final Collection<String> roles = realmAccess.get(CLAIM_ROLES);

      // Check if any roles are present
      if (roles != null && !roles.isEmpty()) {

        // Iterate of the roles and add them to the granted authorities
        final Collection<GrantedAuthority> realmRoles = roles.stream()
                // Prefix all realm roles with "ROLE_"
                .map(String::toUpperCase)
                .map(role -> PREFIX_ROLE + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        grantedAuthorities.addAll(realmRoles);
      }
    }

    return grantedAuthorities;
  }
}
