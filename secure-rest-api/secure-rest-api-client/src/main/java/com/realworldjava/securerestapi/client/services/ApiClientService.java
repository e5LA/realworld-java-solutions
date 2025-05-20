package com.realworldjava.securerestapi.client.services;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.USER_AGENT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

public class ApiClientService {

  private static final Logger log = LoggerFactory.getLogger(ApiClientService.class);
  private static final String CLIENT_HEADER = "secure-rest-api-client/1.0";
  private final RestClient restClient;
  private final String apiEndpoint;

  public ApiClientService(RestClient restClient, String apiEndpoint) {
    this.restClient = restClient;
    this.apiEndpoint = apiEndpoint;
  }

  public void callApi(String token) {
    try {
      String response = restClient.get()
          .uri(apiEndpoint)
          .header(AUTHORIZATION, "Bearer " + token)
          .header(USER_AGENT, CLIENT_HEADER)
          .retrieve()
          .body(String.class);

      log.info("Response from secure API: {}", response);
    } catch (HttpClientErrorException | HttpServerErrorException ex) {
      log.error("Failed to call secure API: {}", ex.getResponseBodyAsString(), ex);
    }
  }
}