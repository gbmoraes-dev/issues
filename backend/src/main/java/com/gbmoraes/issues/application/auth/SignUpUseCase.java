package com.gbmoraes.issues.application.auth;

import com.gbmoraes.issues.application.auth.dto.AuthOutput;
import com.gbmoraes.issues.application.auth.dto.SignUpInput;
import com.gbmoraes.issues.application.port.Encoder;
import com.gbmoraes.issues.application.port.Id;
import com.gbmoraes.issues.application.port.Token;
import com.gbmoraes.issues.domain.user.User;
import com.gbmoraes.issues.domain.user.UserException;
import com.gbmoraes.issues.domain.user.UserRepository;

public class SignUpUseCase {

  private final UserRepository userRepository;
  private final Id id;
  private final Encoder encoder;
  private final Token token;

  public SignUpUseCase(
    UserRepository userRepository,
    Id id,
    Encoder encoder,
    Token token
  ) {
    this.userRepository = userRepository;
    this.id = id;
    this.encoder = encoder;
    this.token = token;
  }

  public AuthOutput execute(SignUpInput input) {
    if (userRepository.emailAlreadyTaken(input.email())) {
      throw new UserException.EmailAlreadyInUse(input.email());
    }

    String hash = encoder.hash(input.password());

    User user = User.create(id.generate(), input.name(), input.email(), hash);

    User saved = userRepository.save(user);

    return new AuthOutput(token.generate(saved.id()));
  }
}
