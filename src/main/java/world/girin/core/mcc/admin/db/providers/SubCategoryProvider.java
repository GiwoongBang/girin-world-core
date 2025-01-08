package world.girin.core.mcc.admin.db.providers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import world.girin.core.mcc.admin.db.dtos.requests.SubCategoryRequestDto;
import world.girin.core.mcc.admin.db.dtos.responses.SubCategoryResponseDto;
import world.girin.core.mcc.admin.db.entities.MainCategoryEntity;
import world.girin.core.mcc.admin.db.entities.SubCategoryEntity;
import world.girin.core.mcc.admin.db.interfaces.TableDataProviderInterface;
import world.girin.core.mcc.admin.db.repositories.MainCategoryRepository;
import world.girin.core.mcc.admin.db.repositories.SubCategoryRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service("sub-category")
public class SubCategoryProvider implements TableDataProviderInterface<SubCategoryRequestDto, SubCategoryResponseDto> {

    private final SubCategoryRepository subCategoryRepository;

    private final MainCategoryRepository mainCategoryRepository;

    private static String successMessage = "성공적으로 처리됐습니다.";

    @Override
    public Class<SubCategoryRequestDto> getRequestDtoClass() {

        return SubCategoryRequestDto.class;
    }

    @Override
    public Class<SubCategoryResponseDto> getResponseDtoClass() {

        return SubCategoryResponseDto.class;
    }

    @Override
    public Map<String, Object> getTableData() {

        return Map.of(
                "tableData", subCategoryRepository.findAll()
                        .stream()
                        .map(entity -> {
                            Map<String, Object> subCategoryMap = new HashMap<>();
                            subCategoryMap.put("id", entity.getId());
                            subCategoryMap.put("mainCategory", Map.of(
                                    "id", entity.getMainCategory().getId(),
                                    "type", entity.getMainCategory().getType().getType()
                            ));
                            subCategoryMap.put("name", entity.getName());
                            subCategoryMap.put("lat", entity.getLat());
                            subCategoryMap.put("lng", entity.getLng());
                            subCategoryMap.put("altitude", entity.getAltitude());
                            subCategoryMap.put("description", entity.getDescription());
                            subCategoryMap.put("safetyNotes", entity.getSafetyNotes());
                            subCategoryMap.put("placeInfo", entity.getPlaceInfo());
                            subCategoryMap.put("background", entity.getBackground());
                            subCategoryMap.put("thumbnailImg", entity.getThumbnailImg());
                            subCategoryMap.put("updatedAt", entity.getUpdatedAt());

                            return subCategoryMap;
                        })
                        .collect(Collectors.toList()),

                "selectOptions", Map.of(
                        "mainCategories", mainCategoryRepository.findAll()
                                .stream()
                                .map(mainCategory -> Map.of(
                                        "id", mainCategory.getId(),
                                        "type", mainCategory.getType().getType()
                                ))
                                .collect(Collectors.toList())
                )
        );
    }

    @Transactional
    @Override
    public ResponseEntity<SubCategoryResponseDto> createData(SubCategoryRequestDto requestDto) {
        try {

            MainCategoryEntity mainCategory = mainCategoryRepository.findById(requestDto.getMainCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid MainCategory ID: " + requestDto.getMainCategoryId()));

            SubCategoryEntity subCategoryEntity = SubCategoryEntity.of(requestDto, mainCategory);
            subCategoryRepository.save(subCategoryEntity);

            SubCategoryResponseDto resData = SubCategoryResponseDto.of(subCategoryEntity, successMessage);

            return ResponseEntity.ok(resData);
        } catch (ClassCastException e) {
            SubCategoryResponseDto errorResponse = SubCategoryResponseDto.builder()
                    .errorMessage("잘못된 데이터 형식: " + e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);
        } catch (IllegalArgumentException e) {
            SubCategoryResponseDto errorResponse = SubCategoryResponseDto.builder()
                    .errorMessage("데이터 추가 실패: " + e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            SubCategoryResponseDto errorResponse = SubCategoryResponseDto.builder()
                    .errorMessage("데이터 추가 중 서버 오류가 발생했습니다. || " + e.getMessage())
                    .build();

            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<SubCategoryResponseDto> updateData(Long id, SubCategoryRequestDto requestDto) {
        try {
            SubCategoryEntity existingEntity = subCategoryRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 SubCategory ID: " + id));

            MainCategoryEntity mainCategory = mainCategoryRepository.findById(requestDto.getMainCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid MainCategory ID: " + requestDto.getMainCategoryId()));

            existingEntity.update(requestDto, mainCategory);

            subCategoryRepository.save(existingEntity);

            SubCategoryResponseDto resData = SubCategoryResponseDto.of(existingEntity, successMessage);

            return ResponseEntity.ok(resData);
        } catch (ClassCastException e) {
            SubCategoryResponseDto errorResponse = SubCategoryResponseDto.builder()
                    .errorMessage("잘못된 데이터 형식: " + e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);
        } catch (IllegalArgumentException e) {
            SubCategoryResponseDto errorResponse = SubCategoryResponseDto.builder()
                    .errorMessage("데이터 수정 실패: " + e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            SubCategoryResponseDto errorResponse = SubCategoryResponseDto.builder()
                    .errorMessage("데이터 수정 중 서버 오류가 발생했습니다. || " + e.getMessage())
                    .build();

            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteData(Long id) {
        try {
            SubCategoryEntity entity = subCategoryRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 SubCategory ID: " + id));

            subCategoryRepository.delete(entity);

            return ResponseEntity.ok("SubCategory 데이터가 성공적으로 삭제되었습니다. SubCategory ID: " + id);
        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body("데이터 삭제 실패: " + e.getMessage());
        } catch (Exception e) {

            return ResponseEntity.status(500).body("SubCategory 데이터 삭제 중 서버 오류가 발생했습니다.");
        }
    }

}
