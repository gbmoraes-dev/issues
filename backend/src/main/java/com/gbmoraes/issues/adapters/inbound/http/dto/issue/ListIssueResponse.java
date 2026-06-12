package com.gbmoraes.issues.adapters.inbound.http.dto.issue;

import com.gbmoraes.issues.application.issue.dto.ListIssueOutput;
import java.util.List;

public record ListIssueResponse(
  List<IssueResponse> items,
  String nextCursor,
  boolean hasMore
) {
  public static ListIssueResponse fromOutput(ListIssueOutput output) {
    return new ListIssueResponse(
      output.items().stream().map(IssueResponse::fromOutput).toList(),
      output.nextCursor(),
      output.hasMore()
    );
  }
}
