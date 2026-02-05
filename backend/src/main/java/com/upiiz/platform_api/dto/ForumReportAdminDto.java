package com.upiiz.platform_api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ForumReportAdminDto {
    private Long id;
    private String reporterName;
    private String reporterEmail;

    private Long threadId;
    private String threadTitle;

    private Long postId;
    private String postBodyPreview;

    private String reasonCode;
    private String description;
    private Instant createdAt;
}
