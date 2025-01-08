package world.girin.core.mcc.admin.db.interfaces;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface DataServiceInterface<RequestDto, ResponseDto> {

    ResponseEntity<Map<String, Object>> getInitialDataForTable(String tableName);

    ResponseEntity<ResponseDto> createData(String tableName, RequestDto requestData);

    ResponseEntity<ResponseDto> updateData(String tableName, Long id, RequestDto requestData);

    ResponseEntity<String> deleteData(String tableName, Long id);

}