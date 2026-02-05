package com.upiiz.platform_api.repositories;

import com.upiiz.platform_api.entities.ForumReport;
import com.upiiz.platform_api.models.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForumReportRepository extends JpaRepository<ForumReport, Long> {
    List<ForumReport> findByStatusOrderByCreatedAtAsc(ReportStatus status);

    List<ForumReport> findAllByOrderByCreatedAtDesc();
}