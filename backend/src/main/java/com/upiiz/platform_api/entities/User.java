package com.upiiz.platform_api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "email_inst", length = 120)
    private String emailInst;

    @Column(name = "password_hash", length = 120)
    private String passwordHash;

    @Column(name = "full_name", nullable = false, length = 120)
    private String nombre;

    @Column(name = "carrera", length = 120)
    private String carrera;

    @Column(name = "boleta", length = 120)
    private String boleta;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    // Perfil
    @Column(columnDefinition = "text")
    private String bio;

    // Guardamos JSONB como String (Jackson lo serializa/deserializa en el controller)
    @Column(columnDefinition = "jsonb")
    private String interests;

    @Column(columnDefinition = "jsonb")
    private String links;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "cover_url")
    private String coverUrl;

    // Verificación y aprobación para cuentas LOCAL
    @Column(name = "email_verified", nullable = false)
    @Builder.Default
    private boolean emailVerified = false;

    @Column(name = "approved", nullable = false)
    @Builder.Default
    private boolean approved = false;

    // Origen de autenticación (LOCAL | GESCO)
    @Column(name = "auth_provider", nullable = false, length = 20)
    @Builder.Default
    private String authProvider = "LOCAL";

    // Identificador externo (boleta GESCO)
    @Column(name = "external_id", length = 120)
    private String externalId;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private Instant updatedAt;

    // Relación con roles
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    // Helpers opcionales
    public void addRole(Role r) {
        if (roles == null) roles = new HashSet<>();
        roles.add(r);
    }
    public void removeRole(Role r) {
        if (roles != null) roles.remove(r);
    }
}
