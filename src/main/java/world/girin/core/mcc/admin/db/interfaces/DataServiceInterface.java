package world.girin.core.mcc.admin.db.interfaces;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface DataServiceInterface<RequestDto, ResponseDto> {

    ResponseEntity<Map<String, Object>> getInitialDataForTable(String tableName);

    ResponseEntity<List<Map<String, Object>>> getDetailsBySubCategoryId(String tableName, Long subCategoryId);

    ResponseEntity<ResponseDto> createData(String tableName, RequestDto requestData);

    ResponseEntity<ResponseDto> updateData(String tableName, Long id, RequestDto requestData);

    ResponseEntity<String> deleteData(String tableName, Long id);

}