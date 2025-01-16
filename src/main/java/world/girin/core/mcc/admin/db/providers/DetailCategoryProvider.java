package world.girin.core.mcc.admin.db.providers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import world.girin.core.mcc.admin.db.dtos.requests.DetailCategoryRequestDto;
import world.girin.core.mcc.admin.db.dtos.responses.DetailCategoryResponseDto;
import world.girin.core.mcc.admin.db.entities.DetailCategoryEntity;
import world.girin.core.mcc.admin.db.entities.SubCategoryEntity;
import world.girin.core.mcc.admin.db.enums.DetailCategoryType;
import world.girin.core.mcc.admin.db.interfaces.TableDataProviderInterface;
import world.girin.core.mcc.admin.db.repositories.DetailCategoryRepository;
import world.girin.core.mcc.admin.db.repositories.SubCategoryRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service("detail-category")
public class DetailCategoryProvider implements TableDataProviderInterface<DetailCategoryRequestDto, DetailCategoryResponseDto> {

    private final DetailCategoryRepository detailCategoryRepository;

    private final SubCategoryRepository subCategoryRepository;

    private static String successMessage = "성공적으로 처리됐습니다.";

    @Override
    public Class<DetailCategoryRequestDto> getRequestDtoClass() {

        return DetailCategoryRequestDto.class;
    }

    @Override
    public Class<DetailCategoryResponseDto> getResponseDtoClass() {

        return DetailCategoryResponseDto.class;
    }

    @Override
    public Map<String, Object> getTableData() {

        return Map.of(
                "tableData", detailCategoryRepository.findAll()
                        .stream()
                        .map(entity -> {
                            Map<String, Object> detailCategoryMap = new HashMap<>();
                            detailCategoryMap.put("id", entity.getId());
                            detailCategoryMap.put("subCategory", Map.of(
                                    "id", entity.getSubCategory().getId(),
                                    "name", entity.getSubCategory().getName()
                            ));
                            detailCategoryMap.put("type", entity.getType().getType());
                            detailCategoryMap.put("title", entity.getTitle());
                            detailCategoryMap.put("lat", entity.getLat());
                            detailCategoryMap.put("lng", entity.getLng());
                            detailCategoryMap.put("updatedAt", entity.getUpdatedAt());

                            return detailCategoryMap;
                        })
                        .collect(Collectors.toList()),

                "selectOptions", Map.of(
                        "subCategories", subCategoryRepository.findAll()
                                .stream()
                                .map(subCategory -> Map.of(
                                        "id", subCategory.getId(),
                                        "name", subCategory.getName()
                                ))
                                .collect(Collectors.toList())
                ),

                "enumValues", Map.of(
                        "detailCategoryTypes", Arrays.stream(DetailCategoryType.values())
                                .map(DetailCategoryType::getType)
                                .collect(Collectors.toList())
                )
        );
    }

    @Override
    public List<Map<String, Object>> getDetailsBySubCategoryId(String tableName, Long subCategoryId) {

        if (!"detail-category".equalsIgnoreCase(tableName)) {
            return (List<Map<String, Object>>) ResponseEntity.badRequest().body(List.of(Map.of("error", "Invalid table name: " + tableName)));
        }

        try {
            SubCategoryEntity subCategory = subCategoryRepository.findById(subCategoryId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 SubCategory ID: " + subCategoryId));

            List<Map<String, Object>> detailCategories =
                    detailCategoryRepository.findBySubCategory(subCategory)
                            .stream()
                            .map(entity -> Map.of(
                                    "id", entity.getId(),
                                    "subCategory", Map.of(
                                            "id", entity.getSubCategory().getId(),
                                            "name", entity.getSubCategory().getName()
                                    ),
                                    "type", entity.getType().getType(),
                                    "title", entity.getTitle(),
                                    "lat", entity.getLat(),
                                    "lng", entity.getLng(),
                                    "updatedAt", entity.getUpdatedAt()
                            ))
                            .toList();

            return detailCategories;
        } catch (IllegalArgumentException e) {
            return (List<Map<String, Object>>) ResponseEntity.badRequest().body("데이터 조회 실패: " + e.getMessage());
        } catch (Exception e) {
            return (List<Map<String, Object>>) ResponseEntity.status(500).body("데이터 조회 중 서버 오류가 발생했습니다.");
        }
    }

    @Transactional
    @Override
    public ResponseEntity<DetailCategoryResponseDto> createData(DetailCategoryRequestDto requestDto) {

        try {
            SubCategoryEntity subCategory = subCategoryRepository.findById(requestDto.getSubCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid SubCategory ID: " + requestDto.getSubCategoryId()));

            DetailCategoryType type = DetailCategoryType.fromType(requestDto.getType());

            DetailCategoryEntity detailCategory = DetailCategoryEntity.of(requestDto, subCategory, type);
            detailCategoryRepository.save(detailCategory);

            DetailCategoryResponseDto resData = DetailCategoryResponseDto.of(detailCategory, successMessage);

            return ResponseEntity.ok(resData);
        } catch (IllegalArgumentException e) {
            DetailCategoryResponseDto errorResponse = DetailCategoryResponseDto.builder()
                    .errorMessage("데이터 추가 실패: " + e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            DetailCategoryResponseDto errorResponse = DetailCategoryResponseDto.builder()
                    .errorMessage("데이터 추가 중 서버 오류가 발생했습니다. || " + e.getMessage())
                    .build();

            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<DetailCategoryResponseDto> updateData(Long id, DetailCategoryRequestDto requestDto) {
        try {
            DetailCategoryEntity existingEntity = detailCategoryRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 DetailCategory ID: " + id));

            SubCategoryEntity subCategory = subCategoryRepository.findById(requestDto.getSubCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid SubCategory ID: " + requestDto.getSubCategoryId()));

            DetailCategoryType type = DetailCategoryType.fromType(requestDto.getType());

            existingEntity.update(requestDto, subCategory, type);

            detailCategoryRepository.save(existingEntity);

            DetailCategoryResponseDto resData = DetailCategoryResponseDto.of(existingEntity, successMessage);

            return ResponseEntity.ok(resData);
        } catch (ClassCastException e) {
            DetailCategoryResponseDto errorResponse = DetailCategoryResponseDto.builder()
                    .errorMessage("잘못된 데이터 형식: " + e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);
        } catch (IllegalArgumentException e) {
            DetailCategoryResponseDto errorResponse = DetailCategoryResponseDto.builder()
                    .errorMessage("데이터 수정 실패: " + e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            DetailCategoryResponseDto errorResponse = DetailCategoryResponseDto.builder()
                    .errorMessage("데이터 수정 중 서버 오류가 발생했습니다. || " + e.getMessage())
                    .build();

            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteData(Long id) {
        try {
            DetailCategoryEntity existingEntity = detailCategoryRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 DetailCategory ID: " + id));

            detailCategoryRepository.delete(existingEntity);

            return ResponseEntity.ok("데이터가 성공적으로 삭제되었습니다. DetailCategory ID: " + id);
        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body("DetailCategory 데이터 삭제 실패: " + e.getMessage());
        } catch (Exception e) {

            return ResponseEntity.status(500).body("DetailCategory 데이터 삭제 중 오류가 발생했습니다.");
        }
    }

}