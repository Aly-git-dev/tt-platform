package com.upiiz.platform_api.entities;

import com.upiiz.platform_api.entities.User;
import com.upiiz.platform_api.models.PostStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "forum_post")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ForumPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "thread_id", nullable = false)
    private ForumThread thread;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_post_id")
    private ForumPost parent;

    @Column(columnDefinition = "text", nullable = false)
    private String body;

    @Column(nullable = false)
    private int score = 0;

    @Column(name = "is_accepted_answer", nullable = false)
    private boolean acceptedAnswer = false;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private PostStatus status = PostStatus.VISIBLE;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<ForumAttachment> attachments = new java.util.ArrayList<>();
}
