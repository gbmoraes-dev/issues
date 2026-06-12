package com.gbmoraes.issues.application.issue.dto;

public record CreateIssueInput(
  String userId,
  String title,
  String description
) {}
