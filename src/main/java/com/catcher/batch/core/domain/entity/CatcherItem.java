package com.catcher.batch.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "delete_at = 'N'")
@Table(name = "catcher_item")
public class CatcherItem extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(unique = true, nullable = false)
    private String itemHashValue;

    @Column(nullable = false)
    private String title;

    private String description;

    private String thumbnailUrl;

    private String resourceUrl;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    @Column(name = "delete_at")
    private ZonedDateTime deletedAt;
}
