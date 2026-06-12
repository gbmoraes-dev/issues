package com.gbmoraes.issues.adapters.outbound.security;

import com.gbmoraes.issues.application.port.Token;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtService implements Token {

  private final SecretKey key;
  private final long expirationMs;

  public JwtService(
    @Value("${jwt.secret}") String secret,
    @Value("${jwt.expiration-ms:86400000}") long expirationMs
  ) {
    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    this.expirationMs = expirationMs;
  }

  @Override
  public String generate(String id) {
    return Jwts.builder()
      .subject(id)
      .issuedAt(new Date())
      .expiration(new Date(System.currentTimeMillis() + expirationMs))
      .signWith(key)
      .compact();
  }

  @Override
  public boolean validate(String token) {
    try {
      Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public String decode(String token) {
    return Jwts.parser()
      .verifyWith(key)
      .build()
      .parseSignedClaims(token)
      .getPayload()
      .getSubject();
  }
}
