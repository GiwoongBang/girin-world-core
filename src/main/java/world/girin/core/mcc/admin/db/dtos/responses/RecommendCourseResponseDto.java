package world.girin.core.mcc.admin.db.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import world.girin.core.mcc.admin.db.dtos.BaseResponseDto;
import world.girin.core.mcc.admin.db.entities.RecommendCourseEntity;

import java.time.LocalDateTime;

@Getter
public class RecommendCourseResponseDto extends BaseResponseDto {

    private Long id;

    private SubCategoryResponseDto subCategory;

    private Long courseId;

    private String title;

    private int order;

    private DetailCategoryResponseDto startPoint;

    private DetailCategoryResponseDto endPoint;

    private String description;

    private double distance;

    private double elevationGain;

    private String thumbnailImg;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    @Builder
    private RecommendCourseResponseDto(Long id, SubCategoryResponseDto subCategory,
                                       Long courseId, String title, int order,
                                       DetailCategoryResponseDto startPoint, DetailCategoryResponseDto endPoint,
                                       String description, double distance, double elevationGain, String thumbnailImg,
                                       LocalDateTime createdAt, LocalDateTime updatedAt, String errorMessage) {
        this.id = id;
        this.subCategory = subCategory;
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
        setErrorMessage(errorMessage);
    }

    public static RecommendCourseResponseDto of(RecommendCourseEntity recommendCourseEntity, String errorMessage) {

        return RecommendCourseResponseDto.builder()
                .id(recommendCourseEntity.getId())
                .subCategory(SubCategoryResponseDto.of(recommendCourseEntity.getSubCategory(), errorMessage))
                .courseId(recommendCourseEntity.getCourseId())
                .title(recommendCourseEntity.getTitle())
                .order(recommendCourseEntity.getOrder())
                .startPoint(DetailCategoryResponseDto.of(recommendCourseEntity.getStartPoint(), errorMessage))
                .endPoint(DetailCategoryResponseDto.of(recommendCourseEntity.getEndPoint(), errorMessage))
                .description(recommendCourseEntity.getDescription())
                .distance(recommendCourseEntity.getDistance())
                .elevationGain(recommendCourseEntity.getElevationGain())
                .thumbnailImg(recommendCourseEntity.getThumbnailImg())
                .createdAt(recommendCourseEntity.getCreatedAt())
                .updatedAt(recommendCourseEntity.getUpdatedAt())
                .errorMessage(errorMessage)
                .build();
    }

    public static RecommendCourseResponseDto withError(String errorMessage) {

        return RecommendCourseResponseDto.builder()
                .errorMessage(errorMessage)
                .build();
    }

}
