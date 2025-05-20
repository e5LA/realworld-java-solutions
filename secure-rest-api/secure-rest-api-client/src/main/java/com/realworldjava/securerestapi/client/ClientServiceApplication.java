package com.realworldjava.securerestapi.client;

import com.realworldjava.securerestapi.client.services.ApiClientService;
import com.realworldjava.securerestapi.client.services.TokenService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class ClientServiceApplication implements CommandLineRunner {

  private static final Logger log = LoggerFactory.getLogger(ClientServiceApplication.class);

  @Value("${secure.api.endpoint}")
  private String apiEndpoint;

  @Value("${keycloak.token-uri}")
  private String tokenEndpoint;

  @Value("${keycloak.client-id}")
  private String clientId;

  @Value("${keycloak.client-secret}")
  private String clientSecret;

  private final RestClient restClient = RestClient.create();
  private TokenService tokenService;
  private ApiClientService apiClientService;

  public static void main(String[] args) {
    SpringApplication.run(ClientServiceApplication.class, args);
  }

  @PostConstruct
  public void init() {
    tokenService = new TokenService(restClient, tokenEndpoint, clientId, clientSecret);
    apiClientService = new ApiClientService(restClient, apiEndpoint);
  }

  @Override
  public void run(String... args) {
    log.info("Calling API with invalid token.");
    try {
      apiClientService.callApi("invalid_token");
    } catch (Exception e) {
      log.warn("Expected failure with invalid token: {}", e.getMessage());
    }

    log.info("Calling API with token.");
    try {
      String token = tokenService.fetchAccessToken();
      apiClientService.callApi(token);
    } catch (Exception e) {
      log.error("Calling API failed", e);
    }
    System.exit(0);
  }
}
