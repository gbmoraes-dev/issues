package com.gbmoraes.issues.unit.issues;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.gbmoraes.issues.application.issue.UpdateIssueUseCase;
import com.gbmoraes.issues.application.issue.dto.IssueOutput;
import com.gbmoraes.issues.application.issue.dto.UpdateIssueInput;
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
class UpdateIssueUseCaseTest {

  @Mock
  IssueRepository issueRepository;

  @InjectMocks
  UpdateIssueUseCase useCase;

  private final Instant now = Instant.now();
  private final Issue issue = new Issue(
    "issue-id",
    "user-id",
    "Old title",
    "Old desc",
    false,
    now,
    now
  );

  @Test
  void shouldUpdateTitleAndReturnOutput() {
    when(issueRepository.findById("issue-id")).thenReturn(issue);
    when(issueRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    IssueOutput result = useCase.execute(
      new UpdateIssueInput("issue-id", "user-id", "New title", null)
    );

    assertThat(result.title()).isEqualTo("New title");
    assertThat(result.description()).isEqualTo("Old desc");
  }

  @Test
  void shouldKeepOldTitleWhenNewTitleIsNull() {
    when(issueRepository.findById("issue-id")).thenReturn(issue);
    when(issueRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    IssueOutput result = useCase.execute(
      new UpdateIssueInput("issue-id", "user-id", null, null)
    );

    assertThat(result.title()).isEqualTo("Old title");
  }

  @Test
  void shouldKeepOldTitleWhenNewTitleIsBlank() {
    when(issueRepository.findById("issue-id")).thenReturn(issue);
    when(issueRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    IssueOutput result = useCase.execute(
      new UpdateIssueInput("issue-id", "user-id", "   ", null)
    );

    assertThat(result.title()).isEqualTo("Old title");
  }

  @Test
  void shouldUpdateDescriptionWhenProvided() {
    when(issueRepository.findById("issue-id")).thenReturn(issue);
    when(issueRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    IssueOutput result = useCase.execute(
      new UpdateIssueInput("issue-id", "user-id", null, "New desc")
    );

    assertThat(result.description()).isEqualTo("New desc");
    assertThat(result.title()).isEqualTo("Old title");
  }

  @Test
  void shouldThrowNotFoundWhenIssueDoesNotExist() {
    when(issueRepository.findById("missing")).thenReturn(null);

    assertThrows(IssueException.NotFound.class, () ->
      useCase.execute(
        new UpdateIssueInput("missing", "user-id", "New title", null)
      )
    );
  }

  @Test
  void shouldThrowUnauthorizedWhenUserDoesNotOwnIssue() {
    when(issueRepository.findById("issue-id")).thenReturn(issue);

    assertThrows(IssueException.Unauthorized.class, () ->
      useCase.execute(
        new UpdateIssueInput("issue-id", "other-user", "New title", null)
      )
    );
  }
}
