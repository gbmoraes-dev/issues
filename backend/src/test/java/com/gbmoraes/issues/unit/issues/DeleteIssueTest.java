package com.gbmoraes.issues.unit.issues;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.gbmoraes.issues.application.issue.DeleteIssueUseCase;
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
class DeleteIssueUseCaseTest {

  @Mock
  IssueRepository issueRepository;

  @InjectMocks
  DeleteIssueUseCase useCase;

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
  void shouldDeleteIssue() {
    when(issueRepository.findById("issue-id")).thenReturn(issue);

    useCase.execute("issue-id", "user-id");

    verify(issueRepository, times(1)).delete("issue-id");
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
