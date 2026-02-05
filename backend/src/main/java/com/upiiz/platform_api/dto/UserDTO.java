package com.upiiz.platform_api.dto;

import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

public class UserDTO {
    private UUID id;
    private String emailInst;
    private String fullName;
    private boolean active;
    private String bio;
    private List<String> interests;
    private List<LinkDTO> links;
    private String avatarUrl;
    private String coverUrl;
    private List<String> roles;

    private String boleta;
    private String programa;

    public static class LinkDTO {
        private String label;
        private String url;

        public LinkDTO() {}
        public LinkDTO(String label, String url) { this.label = label; this.url = url; }
        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
    }

    public UserDTO() {}

    public UserDTO(
            UUID id,
            String emailInst,
            String fullName,
            boolean active,
            String bio,
            @Nullable List<String> interests,
            @Nullable List<LinkDTO> links,
            @Nullable String avatarUrl,
            @Nullable String coverUrl,
            List<String> roles,
            @Nullable String boleta,
            @Nullable String programa
    ) {
        this.id = id;
        this.emailInst = emailInst;
        this.fullName = fullName;
        this.active = active;
        this.bio = bio;
        this.interests = interests;
        this.links = links;
        this.avatarUrl = avatarUrl;
        this.coverUrl = coverUrl;
        this.roles = roles;
        this.boleta = boleta;
        this.programa = programa;
    }

    // getters & setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getEmailInst() { return emailInst; }
    public void setEmailInst(String emailInst) { this.emailInst = emailInst; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public List<String> getInterests() { return interests; }
    public void setInterests(List<String> interests) { this.interests = interests; }

    public List<LinkDTO> getLinks() { return links; }
    public void setLinks(List<LinkDTO> links) { this.links = links; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }

    public String getBoleta() { return boleta; }
    public void setBoleta(String boleta) { this.boleta = boleta; }

    public String getPrograma() { return programa; }
    public void setPrograma(String programa) { this.programa = programa; }
}
