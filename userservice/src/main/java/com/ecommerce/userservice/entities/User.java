package com.ecommerce.userservice.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "token_version", nullable = false)
    @Builder.Default
    private int tokenVersion = 0;

    // ── Password Reset ────────────────────────────────────────────────────────

    @Column(name = "password_reset_code")
    private String passwordResetCode;

    @Column(name = "password_reset_expires")
    private LocalDateTime passwordResetExpires;

    @Column(name = "password_reset_verified", nullable = false)
    @Builder.Default
    private boolean passwordResetVerified = false;

    // ── Timestamps ────────────────────────────────────────────────────────────

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (role == null) role = Role.user;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum Role { user, admin }
}