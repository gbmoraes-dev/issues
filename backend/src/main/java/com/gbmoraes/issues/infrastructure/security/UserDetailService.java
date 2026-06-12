package com.gbmoraes.issues.infrastructure.security;

import com.gbmoraes.issues.adapters.outbound.persistence.user.UserJpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {

  private final UserJpaRepository userJpaRepository;

  public UserDetailService(UserJpaRepository userJpaRepository) {
    this.userJpaRepository = userJpaRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String id)
    throws UsernameNotFoundException {
    var entity = userJpaRepository
      .findById(id)
      .orElseThrow(() ->
        new UsernameNotFoundException("User not found: " + id)
      );

    return User.withUsername(entity.getId())
      .password(entity.getPassword())
      .roles("USER")
      .build();
  }
}
