package world.girin.core.mcc.admin.db.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import world.girin.core.mcc.admin.db.dtos.BaseResponseDto;
import world.girin.core.mcc.admin.db.entities.SubCategoryEntity;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class SubCategoryResponseDto extends BaseResponseDto {

    private Long id;

    private MainCategoryResponseDto mainCategory;

    private String name;

    private double lat;

    private double lng;

    private double altitude;

    private String description;

    private List<String> safetyNotes;

    private String placeInfo;

    private String background;

    private String thumbnailImg;

    private LocalDateTime updatedAt;

    @Builder
    private SubCategoryResponseDto(Long id, MainCategoryResponseDto mainCategory,
                                      String name, double lat, double lng, double altitude,
                                      String description, List<String> safetyNotes, String placeInfo, String background,
                                      LocalDateTime updatedAt, String errorMessage) {
        this.id = id;
        this.mainCategory = mainCategory;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.altitude = altitude;
        this.description = description;
        this.safetyNotes = safetyNotes;
        this.placeInfo = placeInfo;
        this.background = background;
        this.updatedAt = updatedAt;
        setErrorMessage(errorMessage);
    }

    public static SubCategoryResponseDto of(SubCategoryEntity subCategoryEntity, String errorMessage) {
        SubCategoryResponseDto subCategory = SubCategoryResponseDto.builder()
                .id(subCategoryEntity.getId())
                .mainCategory(MainCategoryResponseDto.of(subCategoryEntity.getMainCategory(), errorMessage))
                .name(subCategoryEntity.getName())
                .lat(subCategoryEntity.getLat())
                .lng(subCategoryEntity.getLng())
                .altitude(subCategoryEntity.getAltitude())
                .description(subCategoryEntity.getDescription())
                .safetyNotes(subCategoryEntity.getSafetyNotes())
                .placeInfo(subCategoryEntity.getPlaceInfo())
                .background(subCategoryEntity.getBackground())
                .updatedAt(subCategoryEntity.getUpdatedAt())
                .errorMessage(errorMessage)
                .build();

        return subCategory;
    }

    public static SubCategoryResponseDto withError(String errorMessage) {

        return SubCategoryResponseDto.builder()
                .errorMessage(errorMessage)
                .build();
    }

}
