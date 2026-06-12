package com.gbmoraes.issues.application.issue;

import com.gbmoraes.issues.application.issue.dto.IssueOutput;
import com.gbmoraes.issues.application.issue.dto.UpdateIssueInput;
import com.gbmoraes.issues.domain.issue.Issue;
import com.gbmoraes.issues.domain.issue.IssueException;
import com.gbmoraes.issues.domain.issue.IssueRepository;

public class UpdateIssueUseCase {

  private final IssueRepository issueRepository;

  public UpdateIssueUseCase(IssueRepository issueRepository) {
    this.issueRepository = issueRepository;
  }

  public IssueOutput execute(UpdateIssueInput input) {
    Issue issue = issueRepository.findById(input.id());

    if (issue == null) {
      throw new IssueException.NotFound(input.id());
    }

    if (!issue.userId().equals(input.userId())) {
      throw new IssueException.Unauthorized(input.id());
    }

    String newTitle = (input.title() != null && !input.title().isBlank())
      ? input.title()
      : issue.title();
    String newDescription =
      input.description() != null ? input.description() : issue.description();

    Issue updated = issue.withUpdates(newTitle, newDescription);

    return IssueOutput.fromDomain(issueRepository.save(updated));
  }
}
