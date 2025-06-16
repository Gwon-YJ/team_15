package github.npcamp.teamtaskflow.global.payload;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final LocalDateTime timestamp;
    private final T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "요청이 성공했습니다.", LocalDateTime.now(), data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, LocalDateTime.now(), data);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(true, message, LocalDateTime.now(), null);
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(true, "요청이 성공했습니다.", LocalDateTime.now(), null);
    }

    public static <T> ApiResponse<T> failure(String message) {
        return new ApiResponse<>(false, message, LocalDateTime.now(), null);
    }

    public static <T> ApiResponse<T> failure(T data) {
        return new ApiResponse<>(false, "요청이 실패했습니다.", LocalDateTime.now(), data);
    }

}

