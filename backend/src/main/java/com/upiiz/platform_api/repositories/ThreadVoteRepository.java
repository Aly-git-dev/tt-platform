package com.upiiz.platform_api.repositories;

import com.upiiz.platform_api.entities.ForumThread;
import com.upiiz.platform_api.entities.ThreadVote;
import com.upiiz.platform_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThreadVoteRepository extends JpaRepository<ThreadVote, Long> {
    Optional<ThreadVote> findByUserAndThread(User user, ForumThread thread);
}
