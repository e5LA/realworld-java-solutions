package com.realworldjava.securerestapi.server;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
    "spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8081/realms/demo",
    "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8081/realms/demo/certs"
})
@AutoConfigureMockMvc
class ApiServerApplicationIntegrationTests {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @WithMockUser(roles = {"READER"})
  void getAllProducts_asReader_returnsOk() throws Exception {
    mockMvc.perform(get("/products")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
  }

  @Test
  @WithMockUser(roles = {"GUEST"})
  void getAllProducts_asGuest_returnsForbidden() throws Exception {
    mockMvc.perform(get("/products")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }
    
}
