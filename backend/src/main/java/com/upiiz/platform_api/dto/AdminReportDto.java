package com.upiiz.platform_api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class AdminReportDto {

    private Long id;

    // Info del reportante
    private String reporterId;
    private String reporterName;

    // Info del contenido reportado
    private Long threadId;
    private String threadTitle;
    private Long postId;
    private String reportedUserId;
    private String reportedUserName;

    // Detalles del reporte
    private String reasonCode;
    private String description;
    private String status;

    private Instant createdAt;
    private Instant handledAt;
    private String handledByName;
}
