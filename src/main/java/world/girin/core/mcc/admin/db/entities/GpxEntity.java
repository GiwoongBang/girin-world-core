package world.girin.core.mcc.admin.db.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import world.girin.core.mcc.admin.db.dtos.requests.GpxRequestDto;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "mcc_gpx")
@Entity
public class GpxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id", referencedColumnName = "id", nullable = false)
    private SubCategoryEntity subCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "start_point_id", referencedColumnName = "id", nullable = false)
    private DetailCategoryEntity startPoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_point_id", referencedColumnName = "id", nullable = false)
    private DetailCategoryEntity endPoint;

    @Column(name="gpx_url", nullable = false)
    private String gpxUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public GpxEntity(SubCategoryEntity subCategory, DetailCategoryEntity startPoint, DetailCategoryEntity endPoint,
                                String gpxUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.subCategory = subCategory;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.gpxUrl = gpxUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static GpxEntity of(GpxRequestDto dto, SubCategoryEntity subCategory,
                               DetailCategoryEntity startPoint, DetailCategoryEntity endPoint) {

        return GpxEntity.builder()
                .subCategory(subCategory)
                .startPoint(startPoint)
                .endPoint(endPoint)
                .gpxUrl(dto.getGpxUrl())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public void update(GpxRequestDto dto, SubCategoryEntity subCategory,
                       DetailCategoryEntity startPoint, DetailCategoryEntity endPoint, LocalDateTime createdAt) {
        this.subCategory = subCategory;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.gpxUrl = dto.getGpxUrl();
        this.createdAt = createdAt;
        this.updatedAt = LocalDateTime.now();
    }

}
