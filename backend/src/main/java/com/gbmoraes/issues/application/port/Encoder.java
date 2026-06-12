package com.gbmoraes.issues.application.port;

public interface Encoder {
  String hash(String raw);
  boolean matches(String raw, String hash);
}
