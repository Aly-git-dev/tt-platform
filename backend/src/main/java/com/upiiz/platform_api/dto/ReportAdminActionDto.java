package com.upiiz.platform_api.dto;

import lombok.Data;

@Data
public class ReportAdminActionDto {

    private boolean deleteContent;   // true = ocultar/eliminar hilo/post
    private boolean banUser;         // true = desactivar usuario
    private String adminNote;
}
