package com.gbmoraes.issues.application.issue.dto;

import java.util.List;

public record ListIssueOutput(
  List<IssueOutput> items,
  String nextCursor,
  boolean hasMore
) {}
