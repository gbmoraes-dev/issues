package com.gbmoraes.issues.adapters.inbound.http.dto.issue;

import com.gbmoraes.issues.application.issue.dto.CreateIssueInput;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateIssueRequest(
  @NotBlank @Size(max = 100) String title,
  String description
) {
  public CreateIssueInput toInput(String userId) {
    return new CreateIssueInput(
      userId,
      title,
      description != null ? description : ""
    );
  }
}
