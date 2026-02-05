package com.upiiz.platform_api.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostCreateDto {
    private String body;
    private Long parentPostId; // opcional
    private List<AttachmentDto> attachments;
}
