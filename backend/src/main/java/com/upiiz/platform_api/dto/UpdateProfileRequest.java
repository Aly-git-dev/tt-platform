package com.upiiz.platform_api.dto;

import java.util.List;

public record UpdateProfileRequest(
        String fullName,
        String bio,
        List<String> interests,
        List<UserDTO.LinkDTO> links,
        String avatarUrl,
        String coverUrl
) {
}
