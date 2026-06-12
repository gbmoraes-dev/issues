package com.gbmoraes.issues.application.issue.dto;

import com.gbmoraes.issues.domain.issue.Issue;
import java.time.Instant;

public record IssueOutput(
  String id,
  String userId,
  String title,
  String description,
  boolean completed,
  Instant createdAt,
  Instant updatedAt
) {
  public static IssueOutput fromDomain(Issue issue) {
    return new IssueOutput(
      issue.id(),
      issue.userId(),
      issue.title(),
      issue.description(),
      issue.completed(),
      issue.createdAt(),
      issue.updatedAt()
    );
  }
}
