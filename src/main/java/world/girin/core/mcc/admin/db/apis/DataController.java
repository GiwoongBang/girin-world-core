package world.girin.core.mcc.admin.db.apis;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import world.girin.core.mcc.admin.db.interfaces.DataServiceInterface;

@RequiredArgsConstructor
@RequestMapping("/mcc/admin/data")
@RestController
public class DataController<RequestDto, ResponseDto> {

    private final DataServiceInterface<RequestDto, ResponseDto> dataService;

    @GetMapping("/{tableName}")
    public ResponseEntity<?> getTableData(@PathVariable("tableName") String tableName) {

        try {

            return dataService.getInitialDataForTable(tableName);
        } catch (IllegalArgumentException e) {

            return handleBadRequest("유효하지 않은 요청: " + e.getMessage());
        } catch (Exception e) {

            return handleServerError("데이터 조회 중 서버 오류가 발생했습니다.");
        }
    }

    @PostMapping("/{tableName}")
    public ResponseEntity<ResponseDto> createData(
            @PathVariable("tableName") String tableName,
            @RequestBody RequestDto requestDto) {
        try {

            return dataService.createData(tableName, requestDto);
        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().build();
        } catch (Exception e) {

            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{tableName}/{id}")
    public ResponseEntity<ResponseDto> updateData(
            @PathVariable("tableName") String tableName,
            @PathVariable("id") Long id,
            @RequestBody RequestDto
                    requestDto) {

        return dataService.updateData(tableName, id, requestDto);
    }

    @DeleteMapping("/{tableName}/{id}")
    public ResponseEntity<String> deleteData(
            @PathVariable("tableName") String tableName,
            @PathVariable("id") Long id) {

        return dataService.deleteData(tableName, id);
    }

    private ResponseEntity<String> handleBadRequest(String message) {

        return ResponseEntity.badRequest().body(message);
    }

    private ResponseEntity<String> handleServerError(String message) {

        return ResponseEntity.status(500).body(message);
    }

}
