package com.upiiz.platform_api.dto;

import lombok.Data;

@Data
public class ReportCreateDto {
    private Long threadId; // opcional
    private Long postId;   // opcional
    private String reasonCode;
    private String description;
}
