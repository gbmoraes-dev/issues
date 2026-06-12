package com.gbmoraes.issues.adapters.inbound.http.dto.auth;

import com.gbmoraes.issues.application.auth.dto.SignInInput;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignInRequest(@Email String email, @NotBlank String password) {
  public SignInInput toInput() {
    return new SignInInput(email, password);
  }
}
