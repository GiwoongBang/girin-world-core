package world.girin.core.mcc.admin.db.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RecommendCourseRequestDto {

    @NotNull(message = "SubCategory ID는 필수입니다.")
    private Long subCategoryId;

    @NotBlank(message = "Title은 필수입니다.")
    private String title;

    @NotNull(message = "startPoint ID는 필수입니다.")
    private Long startPointId;

    @NotNull(message = "endPoint ID는 필수입니다.")
    private Long endPointId;

    private String description;

    private Double distance;

    private Double elevationGain;

    private String thumbnailImg;

}