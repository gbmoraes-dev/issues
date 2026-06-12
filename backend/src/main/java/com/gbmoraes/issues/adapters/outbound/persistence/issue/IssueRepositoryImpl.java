package com.gbmoraes.issues.adapters.outbound.persistence.issue;

import com.gbmoraes.issues.domain.issue.Issue;
import com.gbmoraes.issues.domain.issue.IssueRepository;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class IssueRepositoryImpl implements IssueRepository {

  private final IssueJpaRepository jpa;

  public IssueRepositoryImpl(IssueJpaRepository jpa) {
    this.jpa = jpa;
  }

  @Override
  public Issue save(Issue issue) {
    return jpa.save(IssueJpaEntity.fromDomain(issue)).toDomain();
  }

  @Override
  public Issue findById(String id) {
    return jpa.findById(id).map(IssueJpaEntity::toDomain).orElse(null);
  }

  @Override
  public List<Issue> findAllByUserId(
    String userId,
    Boolean completed,
    String cursor,
    int limit
  ) {
    return jpa
      .findAllByUserIdWithCursor(userId, completed, cursor, limit)
      .stream()
      .map(IssueJpaEntity::toDomain)
      .toList();
  }

  @Override
  public void delete(String id) {
    jpa.deleteById(id);
  }
}
