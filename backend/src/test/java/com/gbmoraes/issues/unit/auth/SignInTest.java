package com.gbmoraes.issues.unit.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.gbmoraes.issues.application.auth.SignInUseCase;
import com.gbmoraes.issues.application.auth.dto.AuthOutput;
import com.gbmoraes.issues.application.auth.dto.SignInInput;
import com.gbmoraes.issues.application.port.Encoder;
import com.gbmoraes.issues.application.port.Token;
import com.gbmoraes.issues.domain.user.User;
import com.gbmoraes.issues.domain.user.UserException;
import com.gbmoraes.issues.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SignInUseCaseTest {

  @Mock
  UserRepository userRepository;

  @Mock
  Encoder encoder;

  @Mock
  Token token;

  @InjectMocks
  SignInUseCase useCase;

  private final User user = new User(
    "user-id",
    "John Doe",
    "johndoe@example.com",
    "hashed"
  );

  @Test
  void shouldReturnTokenWithValidCredentials() {
    when(userRepository.findByEmail("johndoe@example.com")).thenReturn(user);
    when(encoder.matches("secret123", "hashed")).thenReturn(true);
    when(token.generate("user-id")).thenReturn("jwt-token");

    AuthOutput result = useCase.execute(
      new SignInInput("johndoe@example.com", "secret123")
    );

    assertThat(result.token()).isEqualTo("jwt-token");
  }

  @Test
  void shouldThrowInvalidCredentialsWhenUserNotFound() {
    when(userRepository.findByEmail("janedoe@example.com")).thenReturn(null);

    assertThrows(UserException.InvalidCredentials.class, () ->
      useCase.execute(new SignInInput("janedoe@example.com", "secret123"))
    );
  }

  @Test
  void shouldThrowInvalidCredentialsWhenPasswordIsWrong() {
    when(userRepository.findByEmail("johndoe@example.com")).thenReturn(user);
    when(encoder.matches("wrong", "hashed")).thenReturn(false);

    assertThrows(UserException.InvalidCredentials.class, () ->
      useCase.execute(new SignInInput("johndoe@example.com", "wrong"))
    );
  }
}
