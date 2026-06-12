package com.gbmoraes.issues.adapters.outbound.persistence.issue;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IssueJpaRepository
  extends JpaRepository<IssueJpaEntity, String>
{
  @Query(
    """
    SELECT t FROM IssueJpaEntity t
    WHERE t.userId = :userId
    AND (:completed IS NULL OR t.completed = :completed)
    AND (:cursor IS NULL OR t.id < :cursor)
    ORDER BY t.id DESC
    LIMIT :limit
    """
  )
  List<IssueJpaEntity> findAllByUserIdWithCursor(
    String userId,
    Boolean completed,
    String cursor,
    int limit
  );
}
