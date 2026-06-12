package com.gbmoraes.issues.adapters.inbound.http;

import com.gbmoraes.issues.domain.issue.IssueException;
import com.gbmoraes.issues.domain.user.UserException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ProblemDetail> handleValidation(
    MethodArgumentNotValidException ex
  ) {
    Map<String, List<String>> fieldErrors = ex
      .getBindingResult()
      .getFieldErrors()
      .stream()
      .collect(
        Collectors.groupingBy(
          FieldError::getField,
          Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
        )
      );

    ProblemDetail problem = ProblemDetail.forStatusAndDetail(
      HttpStatus.UNPROCESSABLE_CONTENT,
      "Validation failed"
    );

    problem.setProperty("errors", fieldErrors);

    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(problem);
  }

  @ExceptionHandler(UserException.EmailAlreadyInUse.class)
  public ProblemDetail handleEmailInUse(UserException.EmailAlreadyInUse ex) {
    return ProblemDetail.forStatusAndDetail(
      HttpStatus.CONFLICT,
      ex.getMessage() != null ? ex.getMessage() : "Conflict"
    );
  }

  @ExceptionHandler(UserException.InvalidCredentials.class)
  public ProblemDetail handleInvalidCredentials(
    UserException.InvalidCredentials ex
  ) {
    return ProblemDetail.forStatusAndDetail(
      HttpStatus.UNAUTHORIZED,
      ex.getMessage() != null ? ex.getMessage() : "Unauthorized"
    );
  }

  @ExceptionHandler(IssueException.NotFound.class)
  public ProblemDetail handleIssueNotFound(IssueException.NotFound ex) {
    return ProblemDetail.forStatusAndDetail(
      HttpStatus.NOT_FOUND,
      ex.getMessage() != null ? ex.getMessage() : "Not found"
    );
  }

  @ExceptionHandler(IssueException.Unauthorized.class)
  public ProblemDetail handleIssueUnauthorized(IssueException.Unauthorized ex) {
    return ProblemDetail.forStatusAndDetail(
      HttpStatus.FORBIDDEN,
      ex.getMessage() != null ? ex.getMessage() : "Forbidden"
    );
  }

  @ExceptionHandler(IssueException.EmptyTitle.class)
  public ProblemDetail handleIssueEmptyTitle(IssueException.EmptyTitle ex) {
    return ProblemDetail.forStatusAndDetail(
      HttpStatus.UNPROCESSABLE_CONTENT,
      ex.getMessage() != null ? ex.getMessage() : "Unprocessable"
    );
  }
}
