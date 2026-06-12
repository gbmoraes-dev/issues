package com.gbmoraes.issues.application.issue;

import com.gbmoraes.issues.application.issue.dto.IssueOutput;
import com.gbmoraes.issues.application.issue.dto.ListIssueInput;
import com.gbmoraes.issues.application.issue.dto.ListIssueOutput;
import com.gbmoraes.issues.domain.issue.Issue;
import com.gbmoraes.issues.domain.issue.IssueRepository;
import java.util.List;

public class ListIssuesUseCase {

  private final IssueRepository issueRepository;

  public ListIssuesUseCase(IssueRepository issueRepository) {
    this.issueRepository = issueRepository;
  }

  public ListIssueOutput execute(ListIssueInput input) {
    List<Issue> items = issueRepository.findAllByUserId(
      input.userId(),
      input.completed(),
      input.cursor(),
      input.limit() + 1
    );

    boolean hasMore = items.size() > input.limit();
    List<Issue> page = hasMore ? items.subList(0, items.size() - 1) : items;

    return new ListIssueOutput(
      page.stream().map(IssueOutput::fromDomain).toList(),
      hasMore ? page.get(page.size() - 1).id() : null,
      hasMore
    );
  }
}
