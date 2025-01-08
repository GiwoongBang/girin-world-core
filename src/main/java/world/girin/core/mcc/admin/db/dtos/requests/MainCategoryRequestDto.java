package world.girin.core.mcc.admin.db.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MainCategoryRequestDto {

    @NotNull(message = "MainCategory Type은 필수입니다.")
    private String type;

}