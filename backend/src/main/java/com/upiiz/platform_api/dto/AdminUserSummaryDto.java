package com.upiiz.platform_api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AdminUserSummaryDto {
    private UUID id;
    private String emailInst;
    private String fullName;
    private String carrera;
    private boolean active;
}
