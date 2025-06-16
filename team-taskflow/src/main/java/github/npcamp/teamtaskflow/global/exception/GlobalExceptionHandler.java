package github.npcamp.teamtaskflow.global.exception;
import github.npcamp.teamtaskflow.global.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException e) {
        HttpStatus status = e.getErrorCode().getStatus();
        return ResponseEntity.status(status).body(ApiResponse.failure(e.getErrorCode().getMsg()));
    }

}
