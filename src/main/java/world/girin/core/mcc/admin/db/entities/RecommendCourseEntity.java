package world.girin.core.mcc.admin.db.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import world.girin.core.mcc.admin.db.dtos.requests.RecommendCourseRequestDto;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "mcc_recommend_course")
@Entity
public class RecommendCourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id", referencedColumnName = "id", nullable = false)
    private SubCategoryEntity subCategory;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name="course_title", nullable = false)
    private String title;

    @Column(name="course_order", nullable = false)
    private int order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "start_point_id", referencedColumnName = "id", nullable = false)
    private DetailCategoryEntity startPoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_point_id", referencedColumnName = "id", nullable = false)
    private DetailCategoryEntity endPoint;

    @Column
    private String description;

    @Column
    private double distance;

    @Column(name="elevation_gain")
    private double elevationGain;

    @Column(name="thumbnail_image")
    private String thumbnailImg;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public RecommendCourseEntity(SubCategoryEntity subCategoryEntity, Long courseId, String title, int order,
                                 DetailCategoryEntity startPoint, DetailCategoryEntity endPoint,
                                 String description, double distance, double elevationGain, String thumbnailImg,
                                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.subCategory = subCategoryEntity;
        this.courseId = courseId;
        this.title = title;
        this.order = order;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.description = description;
        this.distance = distance;
        this.elevationGain = elevationGain;
        this.thumbnailImg = thumbnailImg;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static RecommendCourseEntity of(
            RecommendCourseRequestDto dto, SubCategoryEntity subCategory,
            Long courseId, int order, DetailCategoryEntity startPoint, DetailCategoryEntity endPoint
    ) {

        return RecommendCourseEntity.builder()
                .subCategory(subCategory)
                .courseId(courseId)
                .title(dto.getTitle())
                .order(order)
                .startPoint(startPoint)
                .endPoint(endPoint)
                .description(dto.getDescription())
                .distance(dto.getDistance())
                .elevationGain(dto.getElevationGain())
                .thumbnailImg(dto.getThumbnailImg())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public void update(RecommendCourseRequestDto dto, SubCategoryEntity subCategory, Long courseId, int order,
                       DetailCategoryEntity startPoint, DetailCategoryEntity endPoint, LocalDateTime createdAt) {
        this.subCategory = subCategory;
        this.courseId = courseId;
        this.title = dto.getTitle();
        this.order = order;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.description = dto.getDescription();
        this.distance = dto.getDistance();
        this.elevationGain = dto.getElevationGain();
        this.thumbnailImg = dto.getThumbnailImg();
        this.createdAt = createdAt;
        this.updatedAt = LocalDateTime.now();
    }

}
