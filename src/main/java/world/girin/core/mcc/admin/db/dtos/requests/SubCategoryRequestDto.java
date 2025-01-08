package world.girin.core.mcc.admin.db.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class SubCategoryRequestDto {

    @NotNull(message = "MainCategory ID는 필수입니다.")
    private Long mainCategoryId;

    @NotBlank(message = "SubCategory Name은 필수입니다.")
    private String name;

    @NotNull(message = "Latitude는 필수입니다.")
    private Double lat;

    @NotNull(message = "Longitude는 필수입니다.")
    private Double lng;

    @NotNull(message = "Altitude는 필수입니다.")
    private Double altitude;

    private String description;

    private List<String> safetyNotes;

    private String placeInfo;

    private String background;

    private String thumbnailImg;

}