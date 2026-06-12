package com.gbmoraes.issues.adapters.inbound.http.dto.issue;

import com.gbmoraes.issues.application.issue.dto.UpdateIssueInput;
import jakarta.validation.constraints.Size;

public record UpdateIssueRequest(
  @Size(max = 100) String title,
  String description
) {
  public UpdateIssueInput toInput(String id, String userId) {
    return new UpdateIssueInput(id, userId, title, description);
  }
}
