package world.girin.core.mcc.admin.db.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import world.girin.core.mcc.admin.db.dtos.BaseResponseDto;
import world.girin.core.mcc.admin.db.entities.DetailCategoryEntity;
import world.girin.core.mcc.admin.db.enums.DetailCategoryType;

import java.time.LocalDateTime;

@Getter
public class DetailCategoryResponseDto extends BaseResponseDto {

    private Long id;

    private SubCategoryResponseDto subCategory;

    private DetailCategoryType type;

    private String title;

    private double lat;

    private double lng;

    private LocalDateTime updatedAt;


    @Builder
    private DetailCategoryResponseDto(Long id, SubCategoryResponseDto subCategory, DetailCategoryType type,
                                      String title, double lat, double lng,
                                      LocalDateTime updatedAt, String errorMessage) {
        this.id = id;
        this.subCategory = subCategory;
        this.type = type;
        this.title = title;
        this.lat = lat;
        this.lng = lng;
        this.updatedAt = updatedAt;
        setErrorMessage(errorMessage);
    }

    public static DetailCategoryResponseDto of(DetailCategoryEntity detailCategoryEntity, String errorMessage) {

        return DetailCategoryResponseDto.builder()
                .id(detailCategoryEntity.getId())
                .subCategory(SubCategoryResponseDto.of(detailCategoryEntity.getSubCategory(), errorMessage))
                .type(detailCategoryEntity.getType())
                .title(detailCategoryEntity.getTitle())
                .lat(detailCategoryEntity.getLat())
                .lng(detailCategoryEntity.getLng())
                .updatedAt(detailCategoryEntity.getUpdatedAt())
                .errorMessage(errorMessage)
                .build();
    }

    public static DetailCategoryResponseDto withError(String errorMessage) {

        return DetailCategoryResponseDto.builder()
                .errorMessage(errorMessage)
                .build();
    }

}
