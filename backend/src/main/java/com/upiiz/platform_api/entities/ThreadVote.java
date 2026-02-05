package com.upiiz.platform_api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
        name = "thread_vote",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_thread_vote_user_thread",
                columnNames = {"user_id", "thread_id"}
        )
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ThreadVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "thread_id", nullable = false)
    private ForumThread thread;

    @Column(nullable = false)
    private short value; // -1, 0, +1

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;
}
