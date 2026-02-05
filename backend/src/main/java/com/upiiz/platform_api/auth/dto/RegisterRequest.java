package com.upiiz.platform_api.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    private String emailInst;   // debe contener @ipn
    private String fullName;
    private String password;
    private String role;        // "PROFESOR" (por defecto). "ADMIN" solo lo aprobará luego un admin.
}
