package com.gbmoraes.issues.integration.auth.helper;

import com.gbmoraes.issues.adapters.inbound.http.dto.auth.AuthResponse;
import com.gbmoraes.issues.adapters.inbound.http.dto.auth.SignUpRequest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class AuthHelper {

  private final TestRestTemplate restTemplate;

  public AuthHelper(TestRestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public String signUp() {
    return signUp("johndoe@test.com", "secret123");
  }

  public String signUp(String email) {
    return signUp(email, "secret123");
  }

  public String signUp(String email, String password) {
    var response = restTemplate.postForEntity(
      "/auth/sign-up",
      new SignUpRequest(email, "John Doe", password),
      AuthResponse.class
    );
    return response.getBody().token();
  }

  public HttpHeaders authHeaders(String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
  }
}
