package com.gbmoraes.issues.domain.user;

public interface UserRepository {
  User save(User user);
  User findByEmail(String email);
  boolean emailAlreadyTaken(String email);
}
