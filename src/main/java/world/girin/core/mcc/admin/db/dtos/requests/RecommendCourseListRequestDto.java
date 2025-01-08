package world.girin.core.mcc.admin.db.dtos.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
public class RecommendCourseListRequestDto {

    @NotEmpty(message = "RecommendCourseRequestDto 리스트는 비어 있을 수 없습니다.")
    @Valid
    private List<RecommendCourseRequestDto> requestDtos;

}
