package world.girin.core.mcc.admin.db.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import world.girin.core.mcc.admin.db.dtos.BaseResponseDto;
import world.girin.core.mcc.admin.db.entities.TagEntity;

import java.time.LocalDateTime;

@Getter
public class TagResponseDto extends BaseResponseDto {

    private Long id;

    private SubCategoryResponseDto subCategory;

    private String tag;

    private LocalDateTime updatedAt;


    @Builder
    private TagResponseDto(Long id, SubCategoryResponseDto subCategory,
                           String tag, LocalDateTime updatedAt, String errorMessage) {
        this.id = id;
        this.subCategory = subCategory;
        this.tag = tag;
        this.updatedAt = updatedAt;
        setErrorMessage(errorMessage);
    }

    public static TagResponseDto of(TagEntity tagEntity, String errorMessage) {

        return TagResponseDto.builder()
                .id(tagEntity.getId())
                .subCategory(SubCategoryResponseDto.of(tagEntity.getSubCategory(), errorMessage))
                .tag(tagEntity.getTag())
                .updatedAt(tagEntity.getUpdatedAt())
                .errorMessage(errorMessage)
                .build();
    }

    public static TagResponseDto withError(String errorMessage) {

        return TagResponseDto.builder()
                .errorMessage(errorMessage)
                .build();
    }

}
