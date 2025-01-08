package world.girin.core.mcc.admin.db.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TagRequestDto {

    @NotNull(message = "SubCategory ID는 필수입니다.")
    private Long subCategoryId;

    @NotBlank(message = "Tag는 필수입니다.")
    private String tag;

}