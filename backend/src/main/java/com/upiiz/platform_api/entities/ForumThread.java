package com.upiiz.platform_api.entities;

import com.upiiz.platform_api.models.ForumStatus;
import com.upiiz.platform_api.models.ThreadType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "forum_thread")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ForumThread {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private ForumCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subarea_id")
    private ForumSubarea subarea;

    @Column(length = 200, nullable = false)
    private String title;

    @Column(columnDefinition = "text", nullable = false)
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private ThreadType type;   // PREGUNTA / DISCUSSION / ANUNCIO

    @Column(nullable = false)
    private int score = 0;

    @Column(name = "answers_count", nullable = false)
    private int answersCount = 0;

    @Column(nullable = false)
    private int views = 0;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private ForumStatus status = ForumStatus.ABIERTO;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Instant updatedAt;
}
