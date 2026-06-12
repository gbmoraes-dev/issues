package com.gbmoraes.issues.domain.issue;

import java.time.Instant;

public record Issue(
  String id,
  String userId,
  String title,
  String description,
  boolean completed,
  Instant createdAt,
  Instant updatedAt
) {
  public static Issue create(
    String id,
    String userId,
    String title,
    String description
  ) {
    Instant now = Instant.now();
    return new Issue(id, userId, title, description, false, now, now);
  }

  public Issue withUpdates(String title, String description) {
    return new Issue(
      id,
      userId,
      title,
      description,
      completed,
      createdAt,
      Instant.now()
    );
  }

  public Issue toggle() {
    return new Issue(
      id,
      userId,
      title,
      description,
      !completed,
      createdAt,
      Instant.now()
    );
  }
}
