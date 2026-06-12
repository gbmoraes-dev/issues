package com.gbmoraes.issues.integration.auth;

import com.gbmoraes.issues.adapters.inbound.http.dto.auth.AuthResponse;
import com.gbmoraes.issues.adapters.inbound.http.dto.auth.SignInRequest;
import com.gbmoraes.issues.adapters.inbound.http.dto.auth.SignUpRequest;
import com.gbmoraes.issues.config.IntegrationTestBase;
import com.gbmoraes.issues.integration.auth.helper.AuthHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class AuthControllerIntegrationTest extends IntegrationTestBase {

  private AuthHelper authHelper;

  @BeforeEach
  void setup() {
    authHelper = new AuthHelper(restTemplate);
  }

  // ── sign up ───────────────────────────────────────────────────────────────

  @Test
  void postSignUpShouldReturn201AndToken() {
    var response = restTemplate.postForEntity(
      "/auth/sign-up",
      new SignUpRequest("johndoe@test.com", "John Doe", "secret123"),
      AuthResponse.class
    );

    Assertions.assertThat(response.getStatusCode()).isEqualTo(
      HttpStatus.CREATED
    );
    Assertions.assertThat(response.getBody().token()).isNotBlank();
  }

  @Test
  void postSignUpShouldReturn409WhenEmailAlreadyExists() {
    authHelper.signUp("johndoe@test.com", "secret123");

    var response = restTemplate.postForEntity(
      "/auth/sign-up",
      new SignUpRequest("johndoe@test.com", "John Doe", "secret123"),
      String.class
    );

    Assertions.assertThat(response.getStatusCode()).isEqualTo(
      HttpStatus.CONFLICT
    );
  }

  @Test
  void postSignUpShouldReturn422WhenBodyIsInvalid() {
    var response = restTemplate.postForEntity(
      "/auth/sign-up",
      new SignUpRequest("not-an-email", "John Doe", "123"),
      String.class
    );

    Assertions.assertThat(response.getStatusCode().value()).isEqualTo(
      HttpStatus.UNPROCESSABLE_ENTITY.value()
    );
  }

  // ── sign in ───────────────────────────────────────────────────────────────

  @Test
  void postSignInShouldReturn200AndToken() {
    authHelper.signUp("johndoe@test.com", "secret123");

    var response = restTemplate.postForEntity(
      "/auth/sign-in",
      new SignInRequest("johndoe@test.com", "secret123"),
      AuthResponse.class
    );

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    Assertions.assertThat(response.getBody().token()).isNotBlank();
  }

  @Test
  void postSignInShouldReturn401WithWrongPassword() {
    authHelper.signUp("johndoe@test.com", "secret123");

    var response = restTemplate.postForEntity(
      "/auth/sign-in",
      new SignInRequest("johndoe@test.com", "wrongpassword"),
      String.class
    );

    Assertions.assertThat(response.getStatusCode()).isEqualTo(
      HttpStatus.UNAUTHORIZED
    );
  }

  @Test
  void postSignInShouldReturn401ForNonExistentUser() {
    var response = restTemplate.postForEntity(
      "/auth/sign-in",
      new SignInRequest("ghost@test.com", "secret123"),
      String.class
    );

    Assertions.assertThat(response.getStatusCode()).isEqualTo(
      HttpStatus.UNAUTHORIZED
    );
  }
}
