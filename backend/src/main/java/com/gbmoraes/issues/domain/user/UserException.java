package com.gbmoraes.issues.domain.user;

public sealed class UserException
  extends RuntimeException
  permits UserException.EmailAlreadyInUse, UserException.InvalidCredentials
{

  private UserException(String message) {
    super(message);
  }

  public static final class EmailAlreadyInUse extends UserException {

    public EmailAlreadyInUse(String email) {
      super("Email already in use: " + email);
    }
  }

  public static final class InvalidCredentials extends UserException {

    public InvalidCredentials() {
      super("Invalid credentials");
    }
  }
}
