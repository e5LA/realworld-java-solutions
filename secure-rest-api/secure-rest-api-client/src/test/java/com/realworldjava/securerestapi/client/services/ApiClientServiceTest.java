package com.realworldjava.securerestapi.client.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.USER_AGENT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@ExtendWith(MockitoExtension.class)
class ApiClientServiceTest {

  private static final String API_ENDPOINT = "https://api.example.com/resource";
  private static final String AUTH_TOKEN = "test-token";
  private static final String EXPECTED_AUTH_HEADER = "Bearer " + AUTH_TOKEN;
  private static final String CLIENT_HEADER = "secure-rest-api-client/1.0";

  @Mock
  private RestClient restClient;
  @Mock
  private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

  @Mock
  private RestClient.RequestHeadersSpec requestHeadersSpec;

  @Mock
  private RestClient.ResponseSpec responseSpec;

  private ApiClientService apiClientService;

  @BeforeEach
  void setup() {
    apiClientService = new ApiClientService(restClient, API_ENDPOINT);

    // Set up the mock chain
    when(restClient.get()).thenReturn(requestHeadersUriSpec);
    when(requestHeadersUriSpec.uri(API_ENDPOINT)).thenReturn(requestHeadersSpec);
    when(requestHeadersSpec.header(AUTHORIZATION, EXPECTED_AUTH_HEADER)).thenReturn(
        requestHeadersSpec);
    when(requestHeadersSpec.header(USER_AGENT, CLIENT_HEADER)).thenReturn(requestHeadersSpec);
    when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
  }

  @Test
  void callApi_logsResponse_whenTokenValid() {
    // Arrange
    String expectedResponse = "{\"status\": \"success\"}";
    when(responseSpec.body(String.class)).thenReturn(expectedResponse);

    // Act
    assertDoesNotThrow(() -> apiClientService.callApi(AUTH_TOKEN));

    // Assert
    verify(restClient).get();
    verify(requestHeadersUriSpec).uri(API_ENDPOINT);
    verify(requestHeadersSpec).header(HttpHeaders.AUTHORIZATION, EXPECTED_AUTH_HEADER);
    verify(requestHeadersSpec).header(HttpHeaders.USER_AGENT, CLIENT_HEADER);
    verify(responseSpec).body(String.class);
  }

}