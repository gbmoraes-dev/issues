package com.gbmoraes.issues.infrastructure.config;

import com.gbmoraes.issues.application.auth.SignInUseCase;
import com.gbmoraes.issues.application.auth.SignUpUseCase;
import com.gbmoraes.issues.application.issue.*;
import com.gbmoraes.issues.application.port.Encoder;
import com.gbmoraes.issues.application.port.Id;
import com.gbmoraes.issues.application.port.Token;
import com.gbmoraes.issues.domain.issue.IssueRepository;
import com.gbmoraes.issues.domain.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

  @Bean
  public SignUpUseCase signUpUseCase(
    UserRepository userRepository,
    Encoder encoder,
    Token token,
    Id id
  ) {
    return new SignUpUseCase(userRepository, id, encoder, token);
  }

  @Bean
  public SignInUseCase signInUseCase(
    UserRepository userRepository,
    Encoder encoder,
    Token token
  ) {
    return new SignInUseCase(userRepository, encoder, token);
  }

  @Bean
  public CreateIssueUseCase createTodoUseCase(
    IssueRepository issueRepository,
    Id id
  ) {
    return new CreateIssueUseCase(issueRepository, id);
  }

  @Bean
  public GetIssueUseCase getTodoUseCase(IssueRepository issueRepository) {
    return new GetIssueUseCase(issueRepository);
  }

  @Bean
  public ListIssuesUseCase listTodosUseCase(IssueRepository issueRepository) {
    return new ListIssuesUseCase(issueRepository);
  }

  @Bean
  public UpdateIssueUseCase updateTodoUseCase(IssueRepository issueRepository) {
    return new UpdateIssueUseCase(issueRepository);
  }

  @Bean
  public CompleteIssueUseCase completeTodoUseCase(
    IssueRepository issueRepository
  ) {
    return new CompleteIssueUseCase(issueRepository);
  }

  @Bean
  public DeleteIssueUseCase deleteTodoUseCase(IssueRepository issueRepository) {
    return new DeleteIssueUseCase(issueRepository);
  }
}
