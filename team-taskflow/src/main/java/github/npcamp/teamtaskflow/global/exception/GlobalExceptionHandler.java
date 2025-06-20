package github.npcamp.teamtaskflow.global.exception;

import github.npcamp.teamtaskflow.global.payload.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // @Valid 예외처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(errors));
    }

    // HTTP 메시지 읽기(파싱) 예외처리
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure("요청 데이터의 타입이 올바르지 않습니다."));
    }

    // CustomException 예외처리
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException e) {
        log.error("Exception occurred: ", e); // 전체 스택 트레이스 로그 출력
        HttpStatus status = e.getErrorCode().getStatus();
        return ResponseEntity.status(status).body(ApiResponse.failure(e.getErrorCode().getMsg()));
    }

    // 스프링 시큐리티 인가(Access Denied) 예외처리
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException e) {
        log.error("Exception occurred: ", e); // 전체 스택 트레이스 로그 출력
        return ResponseEntity.status(ErrorCode.ACCESS_DENIED.getStatus()).body(ApiResponse.failure(ErrorCode.ACCESS_DENIED.getMsg()));
    }

    // 예상하지 못한 모든 일반 예외 처리 (마지막 방어선)
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Unexpected error: ", e); // 전체 스택 트레이스 로그 출력
        return ResponseEntity.status(ErrorCode.INVALID_STATUS_TRANSITION.getStatus()).body(ApiResponse.failure(ErrorCode.INTERNAL_SERVER_ERROR.getMsg()));
    }

}
