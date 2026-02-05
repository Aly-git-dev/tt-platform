package com.upiiz.platform_api.repositories;

import com.upiiz.platform_api.entities.ForumPost;
import com.upiiz.platform_api.entities.PostVote;
import com.upiiz.platform_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostVoteRepository extends JpaRepository<PostVote, Long> {
    Optional<PostVote> findByUserAndPost(User user, ForumPost post);
}