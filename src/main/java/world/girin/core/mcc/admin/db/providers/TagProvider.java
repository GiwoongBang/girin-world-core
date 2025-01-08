package world.girin.core.mcc.admin.db.providers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import world.girin.core.mcc.admin.db.dtos.requests.TagRequestDto;
import world.girin.core.mcc.admin.db.dtos.responses.TagResponseDto;
import world.girin.core.mcc.admin.db.entities.SubCategoryEntity;
import world.girin.core.mcc.admin.db.entities.TagEntity;
import world.girin.core.mcc.admin.db.interfaces.TableDataProviderInterface;
import world.girin.core.mcc.admin.db.repositories.SubCategoryRepository;
import world.girin.core.mcc.admin.db.repositories.TagRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service("tag")
public class TagProvider implements TableDataProviderInterface<TagRequestDto, TagResponseDto> {

    private final TagRepository tagRepository;

    private final SubCategoryRepository subCategoryRepository;

    private static String successMessage = "성공적으로 처리됐습니다.";

    @Override
    public Class<TagRequestDto> getRequestDtoClass() {

        return TagRequestDto.class;
    }

    @Override
    public Class<TagResponseDto> getResponseDtoClass() {

        return TagResponseDto.class;
    }

    @Override
    public Map<String, Object> getTableData() {

        return Map.of(
                "tableData", tagRepository.findAll()
                        .stream()
                        .map(entity -> {
                            Map<String, Object> tagMap = new HashMap<>();
                            tagMap.put("id", entity.getId());
                            tagMap.put("subCategory", Map.of(
                                    "id", entity.getSubCategory().getId(),
                                    "name", entity.getSubCategory().getName()
                            ));
                            tagMap.put("tag", entity.getTag());
                            tagMap.put("updatedAt", entity.getUpdatedAt());

                            return tagMap;
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
                )
        );
    }

    @Transactional
    @Override
    public ResponseEntity<TagResponseDto> createData(TagRequestDto requestDto) {
        try {
            SubCategoryEntity subCategory = subCategoryRepository.findById(requestDto.getSubCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid SubCategory ID: " + requestDto.getSubCategoryId()));

            TagEntity tagEntity = TagEntity.of(requestDto, subCategory);
            tagRepository.save(tagEntity);

            TagResponseDto resData = TagResponseDto.of(tagEntity, successMessage);

            return ResponseEntity.ok(resData);
        } catch (ClassCastException e) {
            TagResponseDto errorResponse = TagResponseDto.builder()
                    .errorMessage("잘못된 데이터 형식: " + e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);
        } catch (IllegalArgumentException e) {
            TagResponseDto errorResponse = TagResponseDto.builder()
                    .errorMessage("데이터 추가 실패: " + e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            TagResponseDto errorResponse = TagResponseDto.builder()
                    .errorMessage("데이터 추가 중 서버 오류가 발생했습니다. || " + e.getMessage())
                    .build();

            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<TagResponseDto> updateData(Long id, TagRequestDto requestDto) {
        try {

            TagEntity existingTag = tagRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Tag ID: " + id));

            SubCategoryEntity subCategory = subCategoryRepository.findById(requestDto.getSubCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid SubCategory ID: " + requestDto.getSubCategoryId()));

            existingTag.update(requestDto, subCategory);

            tagRepository.save(existingTag);

            TagResponseDto resData = TagResponseDto.of(existingTag, successMessage);

            return ResponseEntity.ok(resData);
        } catch (ClassCastException e) {
            TagResponseDto errorResponse = TagResponseDto.builder()
                    .errorMessage("잘못된 데이터 형식: " + e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);
        } catch (IllegalArgumentException e) {
            TagResponseDto errorResponse = TagResponseDto.builder()
                    .errorMessage("데이터 수정 실패: " + e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            TagResponseDto errorResponse = TagResponseDto.builder()
                    .errorMessage("데이터 수정 중 서버 오류가 발생했습니다. || " + e.getMessage())
                    .build();

            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteData(Long id) {
        try {
            TagEntity existingTag = tagRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Tag ID: " + id));

            tagRepository.delete(existingTag);

            return ResponseEntity.ok("Tag 데이터가 성공적으로 삭제되었습니다.");
        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body("Tag 데이터 삭제 실패: " + e.getMessage());
        } catch (Exception e) {

            return ResponseEntity.status(500).body("Tag 데이터 삭제 중 오류가 발생했습니다.");
        }
    }

}
