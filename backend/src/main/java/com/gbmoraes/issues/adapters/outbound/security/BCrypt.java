package com.gbmoraes.issues.adapters.outbound.security;

import com.gbmoraes.issues.application.port.Encoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCrypt implements Encoder {

  private final PasswordEncoder encoder;

  public BCrypt(PasswordEncoder encoder) {
    this.encoder = encoder;
  }

  @Override
  public String hash(String raw) {
    return encoder.encode(raw);
  }

  @Override
  public boolean matches(String raw, String hash) {
    return encoder.matches(raw, hash);
  }
}
