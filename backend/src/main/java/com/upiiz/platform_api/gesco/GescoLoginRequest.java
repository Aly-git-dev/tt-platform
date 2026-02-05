package com.upiiz.platform_api.gesco;

import lombok.*;

@Getter @Setter
@Builder
public class GescoLoginRequest {
    private String username;
    private String password;

    public GescoLoginRequest() {}
    public GescoLoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
