package com.gbmoraes.issues.unit.issues;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.gbmoraes.issues.application.issue.ListIssuesUseCase;
import com.gbmoraes.issues.application.issue.dto.ListIssueInput;
import com.gbmoraes.issues.application.issue.dto.ListIssueOutput;
import com.gbmoraes.issues.domain.issue.Issue;
import com.gbmoraes.issues.domain.issue.IssueRepository;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ListIssuesUseCaseTest {

  @Mock
  IssueRepository issueRepository;

  @InjectMocks
  ListIssuesUseCase useCase;

  private Issue issue(String id) {
    Instant now = Instant.now();
    return new Issue(id, "user-id", "Title", "desc", false, now, now);
  }

  @Test
  void shouldReturnPageWithHasMoreFalseWhenNoMore() {
    when(issueRepository.findAllByUserId("user-id", null, null, 3)).thenReturn(
      List.of(issue("1"), issue("2"))
    );

    ListIssueOutput result = useCase.execute(
      new ListIssueInput("user-id", null, null, 2)
    );

    assertThat(result.items()).hasSize(2);
    assertThat(result.hasMore()).isFalse();
    assertThat(result.nextCursor()).isNull();
  }

  @Test
  void shouldReturnPageWithHasMoreTrueWhenThereAreMore() {
    when(issueRepository.findAllByUserId("user-id", null, null, 3)).thenReturn(
      List.of(issue("1"), issue("2"), issue("3"))
    );

    ListIssueOutput result = useCase.execute(
      new ListIssueInput("user-id", null, null, 2)
    );

    assertThat(result.items()).hasSize(2);
    assertThat(result.hasMore()).isTrue();
    assertThat(result.nextCursor()).isEqualTo("2");
  }
}
