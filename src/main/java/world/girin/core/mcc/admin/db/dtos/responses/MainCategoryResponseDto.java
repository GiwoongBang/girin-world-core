package world.girin.core.mcc.admin.db.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import world.girin.core.mcc.admin.db.dtos.BaseResponseDto;
import world.girin.core.mcc.admin.db.entities.MainCategoryEntity;
import world.girin.core.mcc.admin.db.enums.MainCategoryType;

import java.time.LocalDateTime;

@Getter
public class MainCategoryResponseDto extends BaseResponseDto {

    private Long id;

    private MainCategoryType type;

    private LocalDateTime updatedAt;

    @Builder
    private MainCategoryResponseDto(Long id, MainCategoryType type, LocalDateTime updatedAt, String errorMessage) {
        this.id = id;
        this.type = type;
        this.updatedAt = updatedAt;
        setErrorMessage(errorMessage);
    }

    public static MainCategoryResponseDto of(MainCategoryEntity mainCategoryEntity, String errorMessage) {

        return MainCategoryResponseDto.builder()
                .id(mainCategoryEntity.getId())
                .type(mainCategoryEntity.getType())
                .updatedAt(mainCategoryEntity.getUpdatedAt())
                .errorMessage(errorMessage)
                .build();
    }

    public static MainCategoryResponseDto withError(String errorMessage) {

        return MainCategoryResponseDto.builder()
                .errorMessage(errorMessage)
                .build();
    }

}