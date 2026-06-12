package com.gbmoraes.issues.adapters.outbound.persistence.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository
  extends JpaRepository<UserJpaEntity, String>
{
  UserJpaEntity findByEmail(String email);
  boolean existsByEmail(String email);
}
