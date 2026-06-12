package com.gbmoraes.issues.unit.issues;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.gbmoraes.issues.application.issue.GetIssueUseCase;
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
class GetIssueUseCaseTest {

  @Mock
  IssueRepository issueRepository;

  @InjectMocks
  GetIssueUseCase useCase;

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
  void shouldReturnIssue() {
    when(issueRepository.findById("issue-id")).thenReturn(issue);

    IssueOutput result = useCase.execute("issue-id", "user-id");

    assertThat(result.id()).isEqualTo("issue-id");
    assertThat(result.title()).isEqualTo("Buy milk");
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
