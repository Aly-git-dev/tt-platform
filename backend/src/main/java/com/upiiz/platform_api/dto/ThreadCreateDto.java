package com.upiiz.platform_api.dto;

import lombok.Data;

import java.util.List;

@Data
public class ThreadCreateDto {
    private Long categoryId;
    private Long subareaId;     // opcional
    private String title;
    private String body;
    private String type;        // "PREGUNTA" / "DISCUSSION" / "ANUNCIO"
    List<ForumAttachmentCreateDto> attachments;
}
