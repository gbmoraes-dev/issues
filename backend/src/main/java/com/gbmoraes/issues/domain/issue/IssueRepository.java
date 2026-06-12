package com.gbmoraes.issues.domain.issue;

import java.util.List;

public interface IssueRepository {
  Issue save(Issue issue);
  Issue findById(String id);
  List<Issue> findAllByUserId(
    String userId,
    Boolean completed,
    String cursor,
    int limit
  );
  void delete(String id);
}
