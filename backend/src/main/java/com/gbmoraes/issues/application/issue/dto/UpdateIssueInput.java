package com.gbmoraes.issues.application.issue.dto;

public record UpdateIssueInput(
  String id,
  String userId,
  String title,
  String description
) {}
