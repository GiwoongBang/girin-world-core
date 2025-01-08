package world.girin.core.mcc.admin.db.interfaces;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface TableDataProviderInterface<RequestDto, ResponseDto> {

    Class<RequestDto> getRequestDtoClass();

    Class<ResponseDto> getResponseDtoClass();

    Map<String, Object> getTableData();

    ResponseEntity<ResponseDto> createData(RequestDto requestDto);

    ResponseEntity<ResponseDto> updateData(Long id, RequestDto requestData);

    ResponseEntity<String> deleteData(Long id);



}