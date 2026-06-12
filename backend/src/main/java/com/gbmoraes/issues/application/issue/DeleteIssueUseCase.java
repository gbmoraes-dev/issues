package com.gbmoraes.issues.application.issue;

import com.gbmoraes.issues.domain.issue.Issue;
import com.gbmoraes.issues.domain.issue.IssueException;
import com.gbmoraes.issues.domain.issue.IssueRepository;

public class DeleteIssueUseCase {

  private final IssueRepository issueRepository;

  public DeleteIssueUseCase(IssueRepository issueRepository) {
    this.issueRepository = issueRepository;
  }

  public void execute(String id, String userId) {
    Issue issue = issueRepository.findById(id);

    if (issue == null) {
      throw new IssueException.NotFound(id);
    }

    if (!issue.userId().equals(userId)) {
      throw new IssueException.Unauthorized(id);
    }

    issueRepository.delete(id);
  }
}
