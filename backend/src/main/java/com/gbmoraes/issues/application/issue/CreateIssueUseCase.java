package com.gbmoraes.issues.application.issue;

import com.gbmoraes.issues.application.issue.dto.CreateIssueInput;
import com.gbmoraes.issues.application.issue.dto.IssueOutput;
import com.gbmoraes.issues.application.port.Id;
import com.gbmoraes.issues.domain.issue.Issue;
import com.gbmoraes.issues.domain.issue.IssueException;
import com.gbmoraes.issues.domain.issue.IssueRepository;

public class CreateIssueUseCase {

  private final IssueRepository issueRepository;
  private final Id id;

  public CreateIssueUseCase(IssueRepository issueRepository, Id id) {
    this.issueRepository = issueRepository;
    this.id = id;
  }

  public IssueOutput execute(CreateIssueInput input) {
    if (input.title() == null || input.title().isBlank()) {
      throw new IssueException.EmptyTitle();
    }

    Issue issue = Issue.create(
      id.generate(),
      input.userId(),
      input.title(),
      input.description()
    );

    return IssueOutput.fromDomain(issueRepository.save(issue));
  }
}
