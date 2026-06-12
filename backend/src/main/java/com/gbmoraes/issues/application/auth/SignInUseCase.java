package com.gbmoraes.issues.application.auth;

import com.gbmoraes.issues.application.auth.dto.AuthOutput;
import com.gbmoraes.issues.application.auth.dto.SignInInput;
import com.gbmoraes.issues.application.port.Encoder;
import com.gbmoraes.issues.application.port.Token;
import com.gbmoraes.issues.domain.user.User;
import com.gbmoraes.issues.domain.user.UserException;
import com.gbmoraes.issues.domain.user.UserRepository;

public class SignInUseCase {

  private final UserRepository userRepository;
  private final Encoder encoder;
  private final Token token;

  public SignInUseCase(
    UserRepository userRepository,
    Encoder encoder,
    Token token
  ) {
    this.userRepository = userRepository;
    this.encoder = encoder;
    this.token = token;
  }

  public AuthOutput execute(SignInInput input) {
    User user = userRepository.findByEmail(input.email());

    if (user == null) {
      throw new UserException.InvalidCredentials();
    }

    if (!encoder.matches(input.password(), user.password())) {
      throw new UserException.InvalidCredentials();
    }

    return new AuthOutput(token.generate(user.id()));
  }
}
