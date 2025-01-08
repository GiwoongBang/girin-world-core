package world.girin.core.mcc.admin.db.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import world.girin.core.mcc.admin.db.interfaces.DataServiceInterface;
import world.girin.core.mcc.admin.db.interfaces.TableDataProviderInterface;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class DataServiceImpl<RequestDto, ResponseDto> implements DataServiceInterface<RequestDto, ResponseDto> {

    private final ApplicationContext applicationContext;

    @Override
    public ResponseEntity<Map<String, Object>> getInitialDataForTable(String tableName) {
        try {
            TableDataProviderInterface<RequestDto, ResponseDto> provider = getProvider(tableName);

            return ResponseEntity.ok(provider.getTableData());
        } catch (Exception e) {

            return ResponseEntity.badRequest().body(Map.of(
                    "error", "유효하지 않은 테이블 이름: " + tableName
            ));
        }
    }

    @Override
    public ResponseEntity<ResponseDto> createData(String tableName, RequestDto requestDto) {
        try {
            TableDataProviderInterface<RequestDto, ResponseDto> provider = getProvider(tableName);

            if (requestDto instanceof Map) {
                ObjectMapper mapper = new ObjectMapper();
                requestDto = mapper.convertValue(requestDto, provider.getRequestDtoClass());
            }

            return provider.createData(requestDto);
        } catch (IllegalArgumentException e) {
            ResponseDto errorResponse = createErrorResponse(tableName, e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            ResponseDto errorResponse = createErrorResponse(tableName, "서버 오류: " + e.getMessage());

            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @Override
    public ResponseEntity<ResponseDto> updateData(String tableName, Long id, RequestDto requestDto) {
        try {
            TableDataProviderInterface<RequestDto, ResponseDto> provider = getProvider(tableName);

            if (requestDto instanceof Map) {
                ObjectMapper mapper = new ObjectMapper();
                requestDto = mapper.convertValue(requestDto, provider.getRequestDtoClass());
            }

            return provider.updateData(id, requestDto);
        } catch (IllegalArgumentException e) {
            ResponseDto errorResponse = createErrorResponse(tableName, e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            ResponseDto errorResponse = createErrorResponse(tableName, "서버 오류: " + e.getMessage());

            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @Override
    public ResponseEntity<String> deleteData(String tableName, Long id) {
        try {
            TableDataProviderInterface<RequestDto, ResponseDto> provider = getProvider(tableName);

            return provider.deleteData(id);
        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body("데이터 삭제 실패: " + e.getMessage());
        } catch (Exception e) {

            return ResponseEntity.status(500).body("데이터 삭제 중 오류가 발생했습니다.");
        }
    }

    private TableDataProviderInterface<RequestDto, ResponseDto> getProvider(String tableName) {
        try {
            TableDataProviderInterface<RequestDto, ResponseDto> provider =
                    (TableDataProviderInterface<RequestDto, ResponseDto>) applicationContext.getBean(tableName, TableDataProviderInterface.class);

            return provider;
        } catch (Exception e) {
            System.err.println("Error retrieving provider: " + e.getMessage());

            throw new IllegalArgumentException("유효하지 않은 테이블 이름: " + tableName);
        }
    }

    private ResponseDto createErrorResponse(String tableName, String errorMessage) {
        try {
            TableDataProviderInterface<RequestDto, ResponseDto> provider = getProvider(tableName);
            Class<ResponseDto> responseClass = provider.getResponseDtoClass();
            return (ResponseDto) responseClass
                    .getDeclaredMethod("withError", String.class)
                    .invoke(null, errorMessage);
        } catch (Exception e) {
            throw new RuntimeException("ErrorResponse 생성 실패: " + e.getMessage(), e);
        }
    }

}