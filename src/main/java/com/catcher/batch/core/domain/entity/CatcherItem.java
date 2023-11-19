package com.catcher.batch.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.time.ZonedDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "deleted_at IS NULL")
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
    private ZonedDateTime startAt;

    @Column(name = "end_at")
    private ZonedDateTime endAt;

    @Column(name = "deleted_at")
    private ZonedDateTime deletedAt;

    public void changeContents(CatcherItem catcherItem) {
        this.category = catcherItem.category;
        this.location = catcherItem.location;
        this.itemHashValue = catcherItem.itemHashValue;
        this.title = catcherItem.title;
        this.description = catcherItem.description;
        this.thumbnailUrl = catcherItem.thumbnailUrl;
        this.resourceUrl = catcherItem.resourceUrl;
        this.startAt = catcherItem.startAt;
        this.endAt = catcherItem.endAt;
    }
}
