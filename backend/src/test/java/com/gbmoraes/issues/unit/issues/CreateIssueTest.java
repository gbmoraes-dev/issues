package com.gbmoraes.issues.unit.issues;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.gbmoraes.issues.application.issue.CreateIssueUseCase;
import com.gbmoraes.issues.application.issue.dto.CreateIssueInput;
import com.gbmoraes.issues.application.issue.dto.IssueOutput;
import com.gbmoraes.issues.application.port.Id;
import com.gbmoraes.issues.domain.issue.IssueException;
import com.gbmoraes.issues.domain.issue.IssueRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateIssueUseCaseTest {

  @Mock
  IssueRepository issueRepository;

  @Mock
  Id id;

  @InjectMocks
  CreateIssueUseCase useCase;

  @Test
  void shouldCreateIssueAndReturnOutput() {
    when(id.generate()).thenReturn("issue-id");
    when(issueRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    IssueOutput result = useCase.execute(
      new CreateIssueInput("user-id", "Buy milk", "From the store")
    );

    assertThat(result.id()).isEqualTo("issue-id");
    assertThat(result.title()).isEqualTo("Buy milk");
    assertThat(result.completed()).isFalse();
    verify(issueRepository, times(1)).save(any());
  }

  @Test
  void shouldThrowEmptyTitleWhenTitleIsBlank() {
    assertThrows(IssueException.EmptyTitle.class, () ->
      useCase.execute(new CreateIssueInput("user-id", "", "description"))
    );
  }

  @Test
  void shouldThrowEmptyTitleWhenTitleIsNull() {
    assertThrows(IssueException.EmptyTitle.class, () ->
      useCase.execute(new CreateIssueInput("user-id", null, "description"))
    );
  }
}
