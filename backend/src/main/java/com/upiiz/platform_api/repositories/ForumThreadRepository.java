package com.upiiz.platform_api.repositories;

import com.upiiz.platform_api.entities.ForumThread;
import com.upiiz.platform_api.models.ForumStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ForumThreadRepository extends JpaRepository<ForumThread, Long> {
    List<ForumThread> findTop5ByStatusOrderByScoreDescCreatedAtDesc(ForumStatus status);
    List<ForumThread> findByCategoryId(Long categoryId);
    long countByAuthorId(UUID authorId);
}
