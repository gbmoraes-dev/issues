package com.gbmoraes.issues.domain.user;

public record User(String id, String name, String email, String password) {
  public static User create(
    String id,
    String name,
    String email,
    String password
  ) {
    return new User(id, name, email, password);
  }
}
