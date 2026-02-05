package com.upiiz.platform_api.repositories;

import com.upiiz.platform_api.entities.ForumSubarea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForumSubareaRepository extends JpaRepository<ForumSubarea, Long> {
    List<ForumSubarea> findByCategoryIdAndActiveTrue(Long categoryId);
}
