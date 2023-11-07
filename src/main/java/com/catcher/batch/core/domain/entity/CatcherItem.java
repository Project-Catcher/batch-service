package com.catcher.batch.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
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

//    @ElementCollection
//    @CollectionTable(
//            name = "item_hash_value_map",
//            joinColumns = {@JoinColumn(name = "catcher_item_id", referencedColumnName = "id")}
//    )
//    @MapKeyColumn(name="item_key")
//    @Column(name = "item", unique = true, nullable = false)
//    private Map<String,Long> itemHashValue = new HashMap<>();

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

//    public void addHashValue(String key,Long value){
//        if (this.itemHashValue == null) {
//            this.itemHashValue = new HashMap<>();
//        }
//
//        this.itemHashValue.put(key,value);
//    }
}
