package com.realworldjava.securerestapi.server.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

class KeycloakJwtAuthenticationConverterTest {

  private KeycloakJwtAuthenticationConverter converter;

  @BeforeEach
  void setup() {
    this.converter = new KeycloakJwtAuthenticationConverter();
  }

  @Test
  void convert_withValidRealmRoles_returnsCorrectAuthorities() {
    Jwt jwt = Jwt.withTokenValue("token")
        .header("alg", "none")
        .claim("realm_access", Map.of("roles", List.of("ADMIN", "READER")))
        .build();

    JwtAuthenticationToken token = (JwtAuthenticationToken) converter.convert(jwt);

    assertEquals(2, token.getAuthorities().size());
    assertTrue(token.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    assertTrue(token.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_READER")));
  }

  @Test
  void convert_withNoRealmAccess_returnsEmptyAuthorities() {
    Jwt jwt = Jwt.withTokenValue("token")
        .header("alg", "none")
        .claim("clm", "vl")
        .build();

    JwtAuthenticationToken token = (JwtAuthenticationToken) converter.convert(jwt);

    assertTrue(token.getAuthorities().isEmpty());
  }

}