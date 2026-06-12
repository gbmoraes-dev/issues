package com.gbmoraes.issues.application.port;

public interface Token {
  String generate(String id);
  boolean validate(String token);
  String decode(String token);
}
