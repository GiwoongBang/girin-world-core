package world.girin.core.mcc.admin.db.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import world.girin.core.mcc.admin.db.dtos.requests.DetailCategoryRequestDto;
import world.girin.core.mcc.admin.db.enums.DetailCategoryType;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Table(name = "mcc_detail_category")
@Entity
public class DetailCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id", referencedColumnName = "id", nullable = false)
    private SubCategoryEntity subCategory;

    @Enumerated(EnumType.STRING)
    @Column(name="detail_category", nullable = false)
    private DetailCategoryType type;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private double lat;

    @Column(nullable = false)
    private double lng;

    @Column(nullable = false)
    private double altitude;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "startPoint", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GpxEntity> startGpx;

    @OneToMany(mappedBy = "endPoint", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GpxEntity> endGpx;

    @OneToMany(mappedBy = "startPoint", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecommendCourseEntity> startRecommendCourse;

    @OneToMany(mappedBy = "endPoint", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecommendCourseEntity> endRecommendCourse;

    @Builder
    public DetailCategoryEntity(SubCategoryEntity subCategory, DetailCategoryType type, String title,
                                double lat, double lng, double altitude, LocalDateTime updatedAt) {
        this.subCategory = subCategory;
        this.type = type;
        this.title = title;
        this.lat = lat;
        this.lng = lng;
        this.altitude = altitude;
        this.updatedAt = updatedAt;
    }

    public static DetailCategoryEntity of(DetailCategoryRequestDto dto,
                                          SubCategoryEntity subCategory,
                                          DetailCategoryType type) {

        return DetailCategoryEntity.builder()
                .subCategory(subCategory)
                .type(type)
                .title(dto.getTitle())
                .lat(dto.getLat())
                .lng(dto.getLng())
                .altitude(dto.getAltitude())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public void update(DetailCategoryRequestDto requestDto,
                       SubCategoryEntity subCategory,
                       DetailCategoryType type) {
        this.subCategory = subCategory;
        this.type = type;
        this.title = requestDto.getTitle();
        this.lat = requestDto.getLat();
        this.lng = requestDto.getLng();
        this.altitude = requestDto.getAltitude();
        this.updatedAt = LocalDateTime.now();
    }

}