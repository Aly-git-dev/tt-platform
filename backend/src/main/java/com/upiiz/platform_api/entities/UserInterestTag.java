package com.upiiz.platform_api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
        name = "user_interest_tag",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_interest_user_cat_sub",
                columnNames = {"user_id", "category_id", "subarea_id"}
        )
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class UserInterestTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private ForumCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subarea_id")
    private ForumSubarea subarea;

    @Column(nullable = false)
    private int weight = 1;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;
}
