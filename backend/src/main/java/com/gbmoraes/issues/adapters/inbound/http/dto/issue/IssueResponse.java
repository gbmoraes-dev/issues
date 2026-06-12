package com.gbmoraes.issues.adapters.inbound.http.dto.issue;

import com.gbmoraes.issues.application.issue.dto.IssueOutput;
import java.time.Instant;

public record IssueResponse(
  String id,
  String userId,
  String title,
  String description,
  boolean completed,
  Instant createdAt,
  Instant updatedAt
) {
  public static IssueResponse fromOutput(IssueOutput output) {
    return new IssueResponse(
      output.id(),
      output.userId(),
      output.title(),
      output.description(),
      output.completed(),
      output.createdAt(),
      output.updatedAt()
    );
  }
}
