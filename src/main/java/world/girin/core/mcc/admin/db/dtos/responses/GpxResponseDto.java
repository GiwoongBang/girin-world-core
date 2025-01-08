package world.girin.core.mcc.admin.db.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import world.girin.core.mcc.admin.db.dtos.BaseResponseDto;
import world.girin.core.mcc.admin.db.entities.GpxEntity;

import java.time.LocalDateTime;

@Getter
public class GpxResponseDto extends BaseResponseDto {

    private Long id;

    private SubCategoryResponseDto subCategory;

    private DetailCategoryResponseDto startPoint;

    private DetailCategoryResponseDto endPoint;

    private String gpxUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    @Builder
    private GpxResponseDto(Long id, SubCategoryResponseDto subCategory,
                           DetailCategoryResponseDto startPoint, DetailCategoryResponseDto endPoint,
                           String gpxUrl, LocalDateTime createdAt, LocalDateTime updatedAt, String errorMessage) {
        this.id = id;
        this.subCategory = subCategory;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.gpxUrl = gpxUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        setErrorMessage(errorMessage);
    }

    public static GpxResponseDto of(GpxEntity gpxEntity, String errorMessage) {

        return GpxResponseDto.builder()
                .id(gpxEntity.getId())
                .subCategory(SubCategoryResponseDto.of(gpxEntity.getSubCategory(), errorMessage))
                .startPoint(DetailCategoryResponseDto.of(gpxEntity.getStartPoint(), errorMessage))
                .endPoint(DetailCategoryResponseDto.of(gpxEntity.getEndPoint(), errorMessage))
                .gpxUrl(gpxEntity.getGpxUrl())
                .createdAt(gpxEntity.getCreatedAt())
                .updatedAt(gpxEntity.getUpdatedAt())
                .errorMessage(errorMessage)
                .build();
    }

    public static GpxResponseDto withError(String errorMessage) {

        return GpxResponseDto.builder()
                .errorMessage(errorMessage)
                .build();
    }

}
