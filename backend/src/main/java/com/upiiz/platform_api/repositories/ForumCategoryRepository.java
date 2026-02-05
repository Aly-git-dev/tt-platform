package com.upiiz.platform_api.repositories;

import com.upiiz.platform_api.entities.ForumCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ForumCategoryRepository extends JpaRepository<ForumCategory, Long> {
    Optional<ForumCategory> findByCode(String code);
    List<ForumCategory> findByActiveTrue();
}
