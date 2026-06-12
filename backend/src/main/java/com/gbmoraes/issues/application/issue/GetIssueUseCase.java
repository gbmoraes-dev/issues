package com.gbmoraes.issues.application.issue;

import com.gbmoraes.issues.application.issue.dto.IssueOutput;
import com.gbmoraes.issues.domain.issue.Issue;
import com.gbmoraes.issues.domain.issue.IssueException;
import com.gbmoraes.issues.domain.issue.IssueRepository;

public class GetIssueUseCase {

  private final IssueRepository issueRepository;

  public GetIssueUseCase(IssueRepository issueRepository) {
    this.issueRepository = issueRepository;
  }

  public IssueOutput execute(String id, String userId) {
    Issue issue = issueRepository.findById(id);

    if (issue == null) {
      throw new IssueException.NotFound(id);
    }

    if (!issue.userId().equals(userId)) {
      throw new IssueException.Unauthorized(id);
    }

    return IssueOutput.fromDomain(issue);
  }
}
