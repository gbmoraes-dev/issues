package com.gbmoraes.issues.adapters.outbound.persistence.issue;

import com.gbmoraes.issues.domain.issue.Issue;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "issues")
public class IssueJpaEntity {

  @Id
  private String id;

  @Column(nullable = false)
  private String userId;

  @Column(nullable = false)
  private String title;

  @Column(nullable = true)
  private String description;

  @Column(nullable = false)
  private boolean completed;

  @Column(nullable = false)
  private Instant createdAt;

  @Column(nullable = false)
  private Instant updatedAt;

  protected IssueJpaEntity() {}

  public IssueJpaEntity(
    String id,
    String userId,
    String title,
    String description,
    boolean completed,
    Instant createdAt,
    Instant updatedAt
  ) {
    this.id = id;
    this.userId = userId;
    this.title = title;
    this.description = description;
    this.completed = completed;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public Issue toDomain() {
    return new Issue(
      id,
      userId,
      title,
      description,
      completed,
      createdAt,
      updatedAt
    );
  }

  public static IssueJpaEntity fromDomain(Issue issue) {
    return new IssueJpaEntity(
      issue.id(),
      issue.userId(),
      issue.title(),
      issue.description(),
      issue.completed(),
      issue.createdAt(),
      issue.updatedAt()
    );
  }

  public String getId() {
    return id;
  }

  public String getUserId() {
    return userId;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public boolean isCompleted() {
    return completed;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }
}
