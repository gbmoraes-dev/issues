package com.gbmoraes.issues.unit.issues;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.gbmoraes.issues.application.issue.CompleteIssueUseCase;
import com.gbmoraes.issues.application.issue.dto.IssueOutput;
import com.gbmoraes.issues.domain.issue.Issue;
import com.gbmoraes.issues.domain.issue.IssueException;
import com.gbmoraes.issues.domain.issue.IssueRepository;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CompleteIssueUseCaseTest {

  @Mock
  IssueRepository issueRepository;

  @InjectMocks
  CompleteIssueUseCase useCase;

  private final Instant now = Instant.now();
  private final Issue issue = new Issue(
    "issue-id",
    "user-id",
    "Buy milk",
    "desc",
    false,
    now,
    now
  );

  @Test
  void shouldCompleteIssue() {
    when(issueRepository.findById("issue-id")).thenReturn(issue);
    when(issueRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    IssueOutput result = useCase.execute("issue-id", "user-id");

    assertThat(result.completed()).isTrue();
  }

  @Test
  void shouldReopenCompletedIssue() {
    Issue completed = new Issue("issue-id", "user-id", "Buy milk", "desc", true, now, now);
    when(issueRepository.findById("issue-id")).thenReturn(completed);
    when(issueRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    IssueOutput result = useCase.execute("issue-id", "user-id");

    assertThat(result.completed()).isFalse();
  }

  @Test
  void shouldThrowNotFoundWhenIssueDoesNotExist() {
    when(issueRepository.findById("missing")).thenReturn(null);

    assertThrows(IssueException.NotFound.class, () ->
      useCase.execute("missing", "user-id")
    );
  }

  @Test
  void shouldThrowUnauthorizedWhenUserDoesNotOwnIssue() {
    when(issueRepository.findById("issue-id")).thenReturn(issue);

    assertThrows(IssueException.Unauthorized.class, () ->
      useCase.execute("issue-id", "other-user")
    );
  }

}
