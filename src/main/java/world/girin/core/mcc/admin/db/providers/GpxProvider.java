package world.girin.core.mcc.admin.db.providers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import world.girin.core.mcc.admin.db.dtos.requests.GpxRequestDto;
import world.girin.core.mcc.admin.db.dtos.responses.GpxResponseDto;
import world.girin.core.mcc.admin.db.entities.DetailCategoryEntity;
import world.girin.core.mcc.admin.db.entities.GpxEntity;
import world.girin.core.mcc.admin.db.entities.SubCategoryEntity;
import world.girin.core.mcc.admin.db.interfaces.TableDataProviderInterface;
import world.girin.core.mcc.admin.db.repositories.DetailCategoryRepository;
import world.girin.core.mcc.admin.db.repositories.GpxRepository;
import world.girin.core.mcc.admin.db.repositories.SubCategoryRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service("gpx")
public class GpxProvider implements TableDataProviderInterface<GpxRequestDto, GpxResponseDto> {

    private final GpxRepository gpxRepository;

    private final SubCategoryRepository subCategoryRepository;

    private final DetailCategoryRepository detailCategoryRepository;

    private static String successMessage = "성공적으로 처리됐습니다.";

    @Override
    public Class<GpxRequestDto> getRequestDtoClass() {

        return GpxRequestDto.class;
    }

    @Override
    public Class<GpxResponseDto> getResponseDtoClass() {

        return GpxResponseDto.class;
    }

    @Override
    public Map<String, Object> getTableData() {

        return Map.of(
                "tableData", gpxRepository.findAll()
                        .stream()
                        .map(entity -> {
                            Map<String, Object> gpxMap = new HashMap<>();
                            gpxMap.put("id", entity.getId());
                            gpxMap.put("subCategory", Map.of(
                                    "id", entity.getSubCategory().getId(),
                                    "name", entity.getSubCategory().getName()
                            ));
                            gpxMap.put("startPoint", Map.of(
                                    "id", entity.getStartPoint().getId(),
                                    "title", entity.getStartPoint().getTitle()
                            ));
                            gpxMap.put("endPoint", Map.of(
                                    "id", entity.getStartPoint().getId(),
                                    "title", entity.getStartPoint().getTitle()
                            ));
                            gpxMap.put("gpxUrl", entity.getGpxUrl());
                            gpxMap.put("createdAt", entity.getCreatedAt());
                            gpxMap.put("updatedAt", entity.getUpdatedAt());

                            return gpxMap;
                        })
                        .collect(Collectors.toList()),

                "selectOptions", Map.of(
                        "subCategories", subCategoryRepository.findAll()
                                .stream()
                                .map(subCategory -> Map.of(
                                        "id", subCategory.getId(),
                                        "name", subCategory.getName()
                                ))
                                .collect(Collectors.toList()),

                        "detailCategories", detailCategoryRepository.findAll()
                                .stream()
                                .map(detailCategory -> Map.of(
                                        "id", detailCategory.getId(),
                                        "title", detailCategory.getTitle()
                                ))
                                .collect(Collectors.toList())
                )
        );
    }

    @Transactional
    @Override
    public ResponseEntity<GpxResponseDto> createData(GpxRequestDto requestDto) {

        try {
            SubCategoryEntity subCategory = subCategoryRepository.findById(requestDto.getSubCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid SubCategory ID: " + requestDto.getSubCategoryId()));

            DetailCategoryEntity startPoint = detailCategoryRepository.findById(requestDto.getStartPointId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid StartPoint ID: " + requestDto.getStartPointId()));

            DetailCategoryEntity endPoint = detailCategoryRepository.findById(requestDto.getEndPointId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid EndPoint ID: " + requestDto.getEndPointId()));

            GpxEntity gpxEntity = GpxEntity.of(requestDto, subCategory, startPoint, endPoint);
            gpxRepository.save(gpxEntity);

            SubCategoryEntity reverseSubCategory = endPoint.getSubCategory();
            GpxEntity reverseGpxEntity = GpxEntity.of(requestDto, reverseSubCategory, endPoint, startPoint);
            gpxRepository.save(reverseGpxEntity);

            GpxResponseDto resData = GpxResponseDto.of(gpxEntity, successMessage);

            return ResponseEntity.ok(resData);
        } catch (ClassCastException e) {
            GpxResponseDto errorResponse = GpxResponseDto.builder()
                    .errorMessage("잘못된 데이터 형식: " + e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);
        } catch (IllegalArgumentException e) {
            GpxResponseDto errorResponse = GpxResponseDto.builder()
                    .errorMessage("데이터 추가 실패: " + e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            GpxResponseDto errorResponse = GpxResponseDto.builder()
                    .errorMessage("데이터 추가 중 서버 오류가 발생했습니다. || " + e.getMessage())
                    .build();

            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<GpxResponseDto> updateData(Long id, GpxRequestDto requestDto) {
        try {

            GpxEntity existingGpx = gpxRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Gpx ID: " + id));

            SubCategoryEntity subCategory = subCategoryRepository.findById(requestDto.getSubCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid SubCategory ID: " + requestDto.getSubCategoryId()));

            DetailCategoryEntity startPoint = detailCategoryRepository.findById(requestDto.getStartPointId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid StartPoint ID: " + requestDto.getStartPointId()));

            DetailCategoryEntity endPoint = detailCategoryRepository.findById(requestDto.getEndPointId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid EndPoint ID: " + requestDto.getEndPointId()));

            existingGpx.update(requestDto, subCategory, startPoint, endPoint, existingGpx.getCreatedAt());

            gpxRepository.save(existingGpx);

            GpxResponseDto resData = GpxResponseDto.of(existingGpx, successMessage);

            return ResponseEntity.ok(resData);
        } catch (ClassCastException e) {
            GpxResponseDto errorResponse = GpxResponseDto.builder()
                    .errorMessage("잘못된 데이터 형식: " + e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);
        } catch (IllegalArgumentException e) {
            GpxResponseDto errorResponse = GpxResponseDto.builder()
                    .errorMessage("데이터 수정 실패: " + e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            GpxResponseDto errorResponse = GpxResponseDto.builder()
                    .errorMessage("데이터 수정 중 서버 오류가 발생했습니다. || " + e.getMessage())
                    .build();

            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteData(Long id) {
        try {
            GpxEntity existingGpx = gpxRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Gpx ID: " + id));

            gpxRepository.delete(existingGpx);

            return ResponseEntity.ok("Gpx 데이터가 성공적으로 삭제되었습니다.");
        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body("Gpx 데이터 삭제 실패: " + e.getMessage());
        } catch (Exception e) {

            return ResponseEntity.status(500).body("Gpx 데이터 삭제 중 오류가 발생했습니다.");
        }
    }

}