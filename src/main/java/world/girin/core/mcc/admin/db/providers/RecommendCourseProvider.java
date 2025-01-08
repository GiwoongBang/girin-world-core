//package world.girin.core.mcc.admin.db.providers;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import world.girin.core.mcc.admin.db.dtos.requests.RecommendCourseListRequestDto;
//import world.girin.core.mcc.admin.db.dtos.requests.RecommendCourseRequestDto;
//import world.girin.core.mcc.admin.db.dtos.responses.RecommendCourseListResponseDto;
//import world.girin.core.mcc.admin.db.dtos.responses.RecommendCourseResponseDto;
//import world.girin.core.mcc.admin.db.entities.DetailCategoryEntity;
//import world.girin.core.mcc.admin.db.entities.RecommendCourseEntity;
//import world.girin.core.mcc.admin.db.entities.SubCategoryEntity;
//import world.girin.core.mcc.admin.db.interfaces.TableDataProviderInterface;
//import world.girin.core.mcc.admin.db.repositories.CourseIdSequenceRepository;
//import world.girin.core.mcc.admin.db.repositories.DetailCategoryRepository;
//import world.girin.core.mcc.admin.db.repositories.RecommendCourseRepository;
//import world.girin.core.mcc.admin.db.repositories.SubCategoryRepository;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@RequiredArgsConstructor
//@Service("recommend-course")
//public class RecommendCourseProvider implements TableDataProviderInterface<RecommendCourseListRequestDto, RecommendCourseListResponseDto> {
//
//    private final RecommendCourseRepository recommendCourseRepository;
//
//    private final SubCategoryRepository subCategoryRepository;
//
//    private final DetailCategoryRepository detailCategoryRepository;
//
//    private final CourseIdSequenceRepository courseIdSequenceRepository;
//
//    private static String successMessage = "성공적으로 처리됐습니다.";
//
//    @Override
//    public Class<RecommendCourseListRequestDto> getRequestDtoClass() {
//
//        return RecommendCourseListRequestDto.class;
//    }
//
//    @Override
//    public Class<RecommendCourseListResponseDto> getResponseDtoClass() {
//
//        return RecommendCourseListResponseDto.class;
//    }
//
//    @Override
//    public Map<String, Object> getTableData() {
//
//        return Map.of(
//                "tableData", recommendCourseRepository.findAll()
//                        .stream()
//                        .map(entity -> {
//                            Map<String, Object> recommendCourseMap = new HashMap<>();
//                            recommendCourseMap.put("id", entity.getId());
//                            recommendCourseMap.put("subCategory", Map.of(
//                                    "id", entity.getSubCategory().getId(),
//                                    "name", entity.getSubCategory().getName()
//                            ));
//                            recommendCourseMap.put("courseId", entity.getCourseId());
//                            recommendCourseMap.put("title", entity.getTitle());
//                            recommendCourseMap.put("order", entity.getOrder());
//                            recommendCourseMap.put("startPoint", Map.of(
//                                    "id", entity.getStartPoint().getId(),
//                                    "title", entity.getStartPoint().getTitle()
//                            ));
//                            recommendCourseMap.put("endPoint", Map.of(
//                                    "id", entity.getStartPoint().getId(),
//                                    "title", entity.getStartPoint().getTitle()
//                            ));
//                            recommendCourseMap.put("description", entity.getDescription());
//                            recommendCourseMap.put("distance", entity.getDistance());
//                            recommendCourseMap.put("elevationGain", entity.getElevationGain());
//                            recommendCourseMap.put("thumbnailImg", entity.getThumbnailImg());
//                            recommendCourseMap.put("createdAt", entity.getCreatedAt());
//                            recommendCourseMap.put("updatedAt", entity.getUpdatedAt());
//
//                            return recommendCourseMap;
//                        })
//                        .collect(Collectors.toList()),
//
//                "selectOptions", Map.of(
//                        "subCategories", subCategoryRepository.findAll()
//                                .stream()
//                                .map(subCategory -> Map.of(
//                                        "id", subCategory.getId(),
//                                        "name", subCategory.getName()
//                                ))
//                                .collect(Collectors.toList()),
//
//                        "detailCategories", detailCategoryRepository.findAll()
//                                .stream()
//                                .map(detailCategory -> Map.of(
//                                        "id", detailCategory.getId(),
//                                        "title", detailCategory.getTitle()
//                                ))
//                                .collect(Collectors.toList())
//                )
//        );
//    }
//
//    @Transactional
//    @Override
//    public ResponseEntity<RecommendCourseListResponseDto> createData(RecommendCourseListRequestDto requestDtos) {
//        try {
//
//            courseIdSequenceRepository.generateNewSequence();
//            Long courseId = courseIdSequenceRepository.getLastGeneratedSequence();
//
//            List<RecommendCourseResponseDto> results = new ArrayList<>();
//
//            List<RecommendCourseRequestDto> recommendCourseRequestDtos = requestDtos.getRequestDtos();
//
//            for (int i = 0; i < recommendCourseRequestDtos.size(); i++) {
//                RecommendCourseRequestDto requestDto = recommendCourseRequestDtos.get(i);
//
//                SubCategoryEntity subCategory = subCategoryRepository.findById(requestDto.getSubCategoryId())
//                        .orElseThrow(() -> new IllegalArgumentException("Invalid SubCategory ID: " + requestDto.getSubCategoryId()));
//
//                DetailCategoryEntity startPoint = detailCategoryRepository.findById(requestDto.getStartPointId())
//                        .orElseThrow(() -> new IllegalArgumentException("Invalid StartPoint ID: " + requestDto.getStartPointId()));
//
//                DetailCategoryEntity endPoint = detailCategoryRepository.findById(requestDto.getEndPointId())
//                        .orElseThrow(() -> new IllegalArgumentException("Invalid EndPoint ID: " + requestDto.getEndPointId()));
//
//                int order = i + 1;
//
//                RecommendCourseEntity recommendCourseEntity = RecommendCourseEntity.of(
//                        requestDto, subCategory, courseId, order, startPoint, endPoint
//                );
//                recommendCourseRepository.save(recommendCourseEntity);
//
//                RecommendCourseResponseDto responseDto = RecommendCourseResponseDto.of(recommendCourseEntity, successMessage);
//                results.add(responseDto);
//            }
//
//            RecommendCourseListResponseDto resData = RecommendCourseListResponseDto.of(results, successMessage);
//
//
//            return ResponseEntity.ok(resData);
//        } catch (ClassCastException e) {
//            RecommendCourseListResponseDto errorResponse = RecommendCourseListResponseDto.builder()
//                    .errorMessage("잘못된 데이터 형식: " + e.getMessage())
//                    .build();
//
//            return ResponseEntity.badRequest().body(errorResponse);
//        } catch (
//                IllegalArgumentException e) {
//            RecommendCourseListResponseDto errorResponse = RecommendCourseListResponseDto.builder()
//                    .errorMessage("데이터 추가 실패: " + e.getMessage())
//                    .build();
//
//            return ResponseEntity.badRequest().body(errorResponse);
//        } catch (
//                Exception e) {
//            RecommendCourseListResponseDto errorResponse = RecommendCourseListResponseDto.builder()
//                    .errorMessage("데이터 추가 중 서버 오류가 발생했습니다. || " + e.getMessage())
//                    .build();
//
//            return ResponseEntity.status(500).body(errorResponse);
//        }
//    }
//
//    @Transactional
//    @Override
//    public ResponseEntity<RecommendCourseResponseDto> updateData(Long id, RecommendCourseRequestDto requestDto) {
//        try {
//
//            RecommendCourseEntity existingCourse = recommendCourseRepository.findById(id)
//                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 RecommendCourse ID: " + id));
//
//            SubCategoryEntity subCategory = subCategoryRepository.findById(requestDto.getSubCategoryId())
//                    .orElseThrow(() -> new IllegalArgumentException("Invalid SubCategory ID: " + requestDto.getSubCategoryId()));
//
//            DetailCategoryEntity startPoint = detailCategoryRepository.findById(requestDto.getStartPointId())
//                    .orElseThrow(() -> new IllegalArgumentException("Invalid StartPoint ID: " + requestDto.getStartPointId()));
//
//            DetailCategoryEntity endPoint = detailCategoryRepository.findById(requestDto.getEndPointId())
//                    .orElseThrow(() -> new IllegalArgumentException("Invalid EndPoint ID: " + requestDto.getEndPointId()));
//
//            Long courseId = existingCourse.getCourseId();
//            int order = existingCourse.getOrder();
//
//            existingCourse.update(requestDto, subCategory, courseId, order, startPoint, endPoint, existingCourse.getCreatedAt());
//            recommendCourseRepository.save(existingCourse);
//
//            RecommendCourseResponseDto resData = RecommendCourseResponseDto.of(existingCourse, successMessage);
//
//            return ResponseEntity.ok(resData);
//        } catch (ClassCastException e) {
//            RecommendCourseResponseDto errorResponse = RecommendCourseResponseDto.builder()
//                    .errorMessage("잘못된 데이터 형식: " + e.getMessage())
//                    .build();
//
//            return ResponseEntity.badRequest().body(errorResponse);
//        } catch (IllegalArgumentException e) {
//            RecommendCourseResponseDto errorResponse = RecommendCourseResponseDto.builder()
//                    .errorMessage("데이터 수정 실패: " + e.getMessage())
//                    .build();
//
//            return ResponseEntity.badRequest().body(errorResponse);
//        } catch (Exception e) {
//            RecommendCourseResponseDto errorResponse = RecommendCourseResponseDto.builder()
//                    .errorMessage("데이터 수정 중 서버 오류가 발생했습니다. || " + e.getMessage())
//                    .build();
//
//            return ResponseEntity.status(500).body(errorResponse);
//        }
//    }
//
//    @Transactional
//    @Override
//    public ResponseEntity<String> deleteData(Long id) {
//        try {
//            RecommendCourseEntity existingCourse = recommendCourseRepository.findById(id)
//                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 RecommendCourse ID: " + id));
//
//            recommendCourseRepository.delete(existingCourse);
//
//            return ResponseEntity.ok("RecommendCourse 데이터가 성공적으로 삭제되었습니다.");
//        } catch (IllegalArgumentException e) {
//
//            return ResponseEntity.badRequest().body("RecommendCourse 데이터 삭제 실패: " + e.getMessage());
//        } catch (Exception e) {
//
//            return ResponseEntity.status(500).body("RecommendCourse 데이터 삭제 중 오류가 발생했습니다.");
//        }
//    }
//
//}
