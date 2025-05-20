package com.realworldjava.securerestapi.server.converters;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class KeycloakJwtAuthenticationConverter implements
    Converter<Jwt, AbstractAuthenticationToken> {

  private static final Logger log = LoggerFactory.getLogger(
      KeycloakJwtAuthenticationConverter.class);

  public static final String PREFIX_ROLE = "ROLE_";
  private static final String CLAIM_REALM_ACCESS = "realm_access";
  private static final String CLAIM_ROLES = "roles";

  @Override
  public AbstractAuthenticationToken convert(Jwt token) {
    return new JwtAuthenticationToken(token, extractRealmRoles(token));
  }

  private Collection<GrantedAuthority> extractRealmRoles(Jwt jwt) {
    Map<String, Object> realmAccess = jwt.getClaim(CLAIM_REALM_ACCESS);
    if (realmAccess == null || !realmAccess.containsKey(CLAIM_ROLES)) {
      log.debug("No realm roles found in token.");
      return List.of();
    }

    Object rolesClaim = realmAccess.get(CLAIM_ROLES);
    if (!(rolesClaim instanceof List<?> rolesList)) {
      log.warn("Expected a list of roles, but got: {}", rolesClaim.getClass());
      return List.of();
    }

    Collection<GrantedAuthority> authorities = rolesList.stream()
        .filter(String.class::isInstance)
        .map(String.class::cast)
        .map(role -> PREFIX_ROLE + role)
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());

    log.debug("Mapped authorities from the token: {}", authorities);

    return authorities;
  }
}
