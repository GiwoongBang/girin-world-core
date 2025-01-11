package world.girin.core.mcc.admin.db.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DetailCategoryRequestDto {

    @NotNull(message = "SubCategory ID는 필수입니다.")
    private Long subCategoryId;

    @NotNull(message = "DetailCategory Type은 필수입니다.")
    private String type;

    @NotBlank(message = "Title은 필수입니다.")
    private String title;

    @NotNull(message = "Latitude는 필수입니다.")
    private Double lat;

    @NotNull(message = "Longitude는 필수입니다.")
    private Double lng;

}