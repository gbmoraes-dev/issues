package com.gbmoraes.issues.application.issue.dto;

public record ListIssueInput(
  String userId,
  Boolean completed,
  String cursor,
  int limit
) {}
