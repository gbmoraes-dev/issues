package com.gbmoraes.issues.domain.issue;

public sealed class IssueException
  extends RuntimeException
  permits
    IssueException.NotFound,
    IssueException.EmptyTitle,
    IssueException.Unauthorized
{

  private IssueException(String message) {
    super(message);
  }

  public static final class NotFound extends IssueException {

    public NotFound(String id) {
      super("Not found: " + id);
    }
  }

  public static final class EmptyTitle extends IssueException {

    public EmptyTitle() {
      super("Title cannot be empty");
    }
  }

  public static final class Unauthorized extends IssueException {

    public Unauthorized(String id) {
      super("You don't have permission to access issue: " + id);
    }
  }
}
