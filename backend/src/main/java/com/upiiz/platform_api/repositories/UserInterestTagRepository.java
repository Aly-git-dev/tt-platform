package com.upiiz.platform_api.repositories;

import com.upiiz.platform_api.entities.User;
import com.upiiz.platform_api.entities.UserInterestTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserInterestTagRepository extends JpaRepository<UserInterestTag, Long> {
    List<UserInterestTag> findByUser(User user);
    long countByUserId(UUID userId);
}
