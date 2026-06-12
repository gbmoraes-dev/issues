package com.gbmoraes.issues.adapters.outbound.persistence.user;

import com.gbmoraes.issues.domain.user.User;
import com.gbmoraes.issues.domain.user.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserRepositoryImpl implements UserRepository {

  private final UserJpaRepository jpa;

  public UserRepositoryImpl(UserJpaRepository jpa) {
    this.jpa = jpa;
  }

  @Override
  public User save(User user) {
    return jpa.save(UserJpaEntity.fromDomain(user)).toDomain();
  }

  @Override
  public User findByEmail(String email) {
    UserJpaEntity entity = jpa.findByEmail(email);

    return entity != null ? entity.toDomain() : null;
  }

  @Override
  public boolean emailAlreadyTaken(String email) {
    return jpa.existsByEmail(email);
  }
}
