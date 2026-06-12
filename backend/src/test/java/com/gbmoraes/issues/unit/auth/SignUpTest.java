package com.gbmoraes.issues.unit.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.gbmoraes.issues.application.auth.SignUpUseCase;
import com.gbmoraes.issues.application.auth.dto.AuthOutput;
import com.gbmoraes.issues.application.auth.dto.SignUpInput;
import com.gbmoraes.issues.application.port.Encoder;
import com.gbmoraes.issues.application.port.Id;
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
class SignUpUseCaseTest {

  @Mock
  UserRepository userRepository;

  @Mock
  Id id;

  @Mock
  Encoder encoder;

  @Mock
  Token token;

  @InjectMocks
  SignUpUseCase useCase;

  @Test
  void shouldReturnTokenOnSuccess() {
    when(userRepository.emailAlreadyTaken("john@example.com")).thenReturn(
      false
    );
    when(id.generate()).thenReturn("user-id");
    when(encoder.hash("secret123")).thenReturn("hashed");
    when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
    when(token.generate("user-id")).thenReturn("jwt-token");

    AuthOutput result = useCase.execute(
      new SignUpInput("John", "john@example.com", "secret123")
    );

    assertThat(result.token()).isEqualTo("jwt-token");
  }

  @Test
  void shouldThrowWhenEmailAlreadyTaken() {
    when(userRepository.emailAlreadyTaken("john@example.com")).thenReturn(true);

    assertThrows(UserException.EmailAlreadyInUse.class, () ->
      useCase.execute(new SignUpInput("John", "john@example.com", "secret123"))
    );
  }
}
