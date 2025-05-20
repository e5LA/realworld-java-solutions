package com.realworldjava.securerestapi.client.services;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;


public class TokenService {

  private static final Logger log = LoggerFactory.getLogger(TokenService.class);
  private final RestClient restClient;
  private final String tokenEndpoint;
  private final String clientId;
  private final String clientSecret;

  public TokenService(RestClient restClient, String tokenEndpoint, String clientId,
      String clientSecret) {
    this.restClient = restClient;
    this.tokenEndpoint = tokenEndpoint;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
  }

  public String fetchAccessToken() {
    MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
    form.add("grant_type", "client_credentials");
    form.add("client_id", clientId);
    form.add("client_secret", clientSecret);

    try {
      Map<String, Object> response = restClient.post()
          .uri(tokenEndpoint)
          .contentType(APPLICATION_FORM_URLENCODED)
          .body(form)
          .retrieve()
          .body(Map.class);

      log.info("Token type: {} expires in: {} seconds", response.get("token_type"),
          response.get("expires_in"));
      return response.get("access_token").toString();

    } catch (HttpClientErrorException | HttpServerErrorException ex) {
      log.error("Failed to fetch token: {}", ex.getResponseBodyAsString(), ex);
      throw new RuntimeException("Token request failed", ex);
    }
  }
}
