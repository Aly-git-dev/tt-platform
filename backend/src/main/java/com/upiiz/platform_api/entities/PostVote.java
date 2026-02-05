package com.upiiz.platform_api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
        name = "post_vote",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_post_vote_user_post",
                columnNames = {"user_id", "post_id"}
        )
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class PostVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private ForumPost post;

    @Column(nullable = false)
    private short value; // -1, 0, +1

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;
}
