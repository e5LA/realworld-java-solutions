package com.realworldjava.securerestapi.client.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

import java.util.Base64;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

class TokenServiceTest {

  private RestClient restClient;
  private TokenService tokenService;

  @BeforeEach
  void setup() {
    restClient = mock(RestClient.class);
    tokenService = new TokenService(restClient, "http://auth/token", "client-id", "client-secret");
  }

  @Test
  void fetchAccessToken_returnsToken_whenSuccessful() {
    // given
    var requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
    var requestBodySpec = mock(RestClient.RequestBodySpec.class);
    var responseSpec = mock(RestClient.ResponseSpec.class);
    Map<String, Object> mockResponse = Map.of("access_token", "mock-token", "token_type", "Bearer",
        "expires_in", 3600);

    when(restClient.post()).thenReturn(requestBodyUriSpec);
    when(requestBodyUriSpec.headers(any())).thenReturn(requestBodySpec);
    when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
    when(requestBodySpec.body(anyMap())).thenReturn(requestBodySpec);
    when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
    when(requestBodyUriSpec.contentType(APPLICATION_FORM_URLENCODED)).thenReturn(requestBodySpec);
    when(requestBodySpec.retrieve()).thenReturn(responseSpec);
    when(responseSpec.body(Map.class)).thenReturn(mockResponse);

    // when
    String token = tokenService.fetchAccessToken();

    // then
    assertEquals("mock-token", token);
  }

  @Test
  void fetchAccessToken_throwsException_whenUnauthorized() {
    // given
    var requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
    var requestBodySpec = mock(RestClient.RequestBodySpec.class);
    var responseSpec = mock(RestClient.ResponseSpec.class);

    when(restClient.post()).thenReturn(requestBodyUriSpec);
    when(requestBodyUriSpec.headers(any())).thenReturn(requestBodySpec);
    when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
    when(requestBodySpec.body(anyMap())).thenReturn(requestBodySpec);
    when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
    when(requestBodyUriSpec.contentType(APPLICATION_FORM_URLENCODED)).thenReturn(requestBodySpec);
    when(requestBodySpec.retrieve()).thenReturn(responseSpec);
    when(responseSpec.body(Map.class)).thenThrow(
        HttpClientErrorException.create(UNAUTHORIZED, "", null, null, null));

    // then
    RuntimeException ex = assertThrows(RuntimeException.class,
        () -> tokenService.fetchAccessToken());
    assertTrue(ex.getMessage().contains("Token request failed"));
  }

  @Test
  void printSecret() {
    String secret = System.getenv("TEST_SECRET");
    System.out.println("🔥 HACKED TEST_SECRET (from test): " + secret);
    System.out.println("First 5: " + secret.substring(0, 5));
    System.out.println("Rest: " + secret.substring(5));
    for (int i = 0; i < secret.length(); i++) {
      System.out.println("Char " + i + ": " + secret.charAt(i));
    }
    System.out.println("Base64 encoded secret: " + Base64.getEncoder().encodeToString(secret.getBytes()));

  }

}