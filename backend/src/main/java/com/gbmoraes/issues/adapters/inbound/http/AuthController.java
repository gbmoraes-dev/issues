package com.gbmoraes.issues.adapters.inbound.http;

import com.gbmoraes.issues.adapters.inbound.http.dto.auth.AuthResponse;
import com.gbmoraes.issues.adapters.inbound.http.dto.auth.SignInRequest;
import com.gbmoraes.issues.adapters.inbound.http.dto.auth.SignUpRequest;
import com.gbmoraes.issues.application.auth.SignInUseCase;
import com.gbmoraes.issues.application.auth.SignUpUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Sign up and sign in to obtain a JWT access token")
public class AuthController {

  private final SignUpUseCase signUpUseCase;
  private final SignInUseCase signInUseCase;

  public AuthController(
    SignUpUseCase signUpUseCase,
    SignInUseCase signInUseCase
  ) {
    this.signUpUseCase = signUpUseCase;
    this.signInUseCase = signInUseCase;
  }

  @Operation(summary = "Create a new account")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Account created — returns a JWT access token"),
    @ApiResponse(responseCode = "409", description = "Email already in use"),
    @ApiResponse(responseCode = "422", description = "Validation error")
  })
  @PostMapping("/sign-up")
  @ResponseStatus(HttpStatus.CREATED)
  public AuthResponse signUp(@Valid @RequestBody SignUpRequest request) {
    return new AuthResponse(signUpUseCase.execute(request.toInput()).token());
  }

  @Operation(summary = "Authenticate and get an access token")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Authentication successful — returns a JWT access token"),
    @ApiResponse(responseCode = "401", description = "Invalid credentials"),
    @ApiResponse(responseCode = "422", description = "Validation error")
  })
  @PostMapping("/sign-in")
  public AuthResponse signIn(@Valid @RequestBody SignInRequest request) {
    return new AuthResponse(signInUseCase.execute(request.toInput()).token());
  }
}
