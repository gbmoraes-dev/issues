package com.gbmoraes.issues.adapters.inbound.http.dto.auth;

import com.gbmoraes.issues.application.auth.dto.SignUpInput;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
  @Email String email,
  @NotBlank String name,
  @Size(min = 8) String password
) {
  public SignUpInput toInput() {
    return new SignUpInput(name, email, password);
  }
}
