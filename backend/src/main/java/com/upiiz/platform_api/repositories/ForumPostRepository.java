package com.upiiz.platform_api.repositories;

import com.upiiz.platform_api.entities.ForumPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ForumPostRepository extends JpaRepository<ForumPost, Long> {
    List<ForumPost> findByThreadIdOrderByCreatedAtAsc(Long threadId);
    long countByAuthorId(UUID authorId);
}
