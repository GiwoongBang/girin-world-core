package world.girin.core.mcc.admin.db.providers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import world.girin.core.mcc.admin.db.dtos.requests.MainCategoryRequestDto;
import world.girin.core.mcc.admin.db.dtos.responses.MainCategoryResponseDto;
import world.girin.core.mcc.admin.db.entities.MainCategoryEntity;
import world.girin.core.mcc.admin.db.enums.MainCategoryType;
import world.girin.core.mcc.admin.db.interfaces.TableDataProviderInterface;
import world.girin.core.mcc.admin.db.repositories.MainCategoryRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service("main-category")
public class MainCategoryProvider implements TableDataProviderInterface<MainCategoryRequestDto, MainCategoryResponseDto> {

    private final MainCategoryRepository mainCategoryRepository;

    private static String successMessage = "성공적으로 처리됐습니다.";

    @Override
    public Class<MainCategoryRequestDto> getRequestDtoClass() {

        return MainCategoryRequestDto.class;
    }

    @Override
    public Class<MainCategoryResponseDto> getResponseDtoClass() {

        return MainCategoryResponseDto.class;
    }

    @Override
    public Map<String, Object> getTableData() {

        return Map.of(
                "tableData", mainCategoryRepository.findAll()
                        .stream()
                        .map(entity -> {
                            Map<String, Object> mainCategoryMap = new HashMap<>();
                            mainCategoryMap.put("id", entity.getId());
                            mainCategoryMap.put("type", entity.getType().getType());
                            mainCategoryMap.put("updatedAt", entity.getUpdatedAt());

                            return mainCategoryMap;
                        })
                        .collect(Collectors.toList()),

                "enumValues", Map.of(
                        "mainCategoryTypes", Arrays.stream(MainCategoryType.values())
                                .map(MainCategoryType::getType)
                                .collect(Collectors.toList())
                )
        );
    }

    @Transactional
    @Override
    public ResponseEntity<MainCategoryResponseDto> createData(MainCategoryRequestDto requestDto) {
        try {

            MainCategoryType type = MainCategoryType.fromType(requestDto.getType());

            MainCategoryEntity mainCategoryEntity = MainCategoryEntity.of(type);
            mainCategoryRepository.save(mainCategoryEntity);

            MainCategoryResponseDto resData = MainCategoryResponseDto.of(mainCategoryEntity, successMessage);

            return ResponseEntity.ok(resData);
        } catch (ClassCastException e) {
            MainCategoryResponseDto errorResponse = MainCategoryResponseDto.builder()
                    .errorMessage("잘못된 데이터 형식: " + e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);
        } catch (IllegalArgumentException e) {
            MainCategoryResponseDto errorResponse = MainCategoryResponseDto.builder()
                    .errorMessage("MainCategory 데이터 추가 실패: " + e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            MainCategoryResponseDto errorResponse = MainCategoryResponseDto.builder()
                    .errorMessage("데이터 추가 중 서버 오류가 발생했습니다. || " + e.getMessage())
                    .build();

            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<MainCategoryResponseDto> updateData(Long id, MainCategoryRequestDto requestDto) {
        try {

            MainCategoryEntity existingMainCategory = mainCategoryRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 MainCategory ID: " + id));

            MainCategoryType type = MainCategoryType.fromType(requestDto.getType());

            existingMainCategory.update(type);

            mainCategoryRepository.save(existingMainCategory);

            MainCategoryResponseDto resData = MainCategoryResponseDto.of(existingMainCategory, successMessage);

            return ResponseEntity.ok(resData);
        } catch (ClassCastException e) {
            MainCategoryResponseDto errorResponse = MainCategoryResponseDto.builder()
                    .errorMessage("잘못된 데이터 형식: " + e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);
        } catch (IllegalArgumentException e) {
            MainCategoryResponseDto errorResponse = MainCategoryResponseDto.builder()
                    .errorMessage("데이터 수정 실패: " + e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            MainCategoryResponseDto errorResponse = MainCategoryResponseDto.builder()
                    .errorMessage("데이터 수정 중 서버 오류가 발생했습니다. || " + e.getMessage())
                    .build();

            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteData(Long id) {
        try {
            MainCategoryEntity existingMainCategory = mainCategoryRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 MainCategory ID: " + id));

            mainCategoryRepository.delete(existingMainCategory);

            return ResponseEntity.ok("MainCategory 데이터가 성공적으로 삭제되었습니다.");
        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body("MainCategory 데이터 삭제 실패: " + e.getMessage());
        } catch (Exception e) {

            return ResponseEntity.status(500).body("MainCategory 데이터 삭제 중 오류가 발생했습니다.");
        }
    }

}
