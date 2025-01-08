package world.girin.core.mcc.admin.db.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import world.girin.core.mcc.admin.db.dtos.BaseResponseDto;

import java.util.List;

@Getter
public class RecommendCourseListResponseDto extends BaseResponseDto {

    private List<RecommendCourseResponseDto> recommendCourses;


    @Builder
    public RecommendCourseListResponseDto(List<RecommendCourseResponseDto> recommendCourses, String errorMessage) {
        this.recommendCourses = recommendCourses;
        setErrorMessage(errorMessage);
    }

    public static RecommendCourseListResponseDto of(List<RecommendCourseResponseDto> recommendCourses, String errorMessage) {

        return RecommendCourseListResponseDto.builder()
                .recommendCourses(recommendCourses)
                .errorMessage(errorMessage)
                .build();
    }

    public static RecommendCourseListResponseDto withError(String errorMessage) {

        return RecommendCourseListResponseDto.builder()
                .errorMessage(errorMessage)
                .build();
    }

}
