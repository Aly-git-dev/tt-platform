package com.upiiz.platform_api.repositories;

import com.upiiz.platform_api.entities.ForumAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForumAttachmentRepository extends JpaRepository<ForumAttachment, Long> {
    List<ForumAttachment> findByPostId(Long postId);

    List<ForumAttachment> findByThreadId(Long threadId);

}
