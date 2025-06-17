package github.npcamp.teamtaskflow.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 공통
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예상하지 못한 에러"),

    // user
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),

    // task
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시물을 찾을 수 없습니다."),
    INVALID_STATUS_TRANSITION(HttpStatus.BAD_REQUEST, "TODO → IN_PROGRESS → DONE 순서로만 변경할 수 있습니다."),

    // comment

    // board

    // log
    ACTIVITY_LOG_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "활동 로그 저장 중 오류가 발생했습니다.");



    private final HttpStatus status;
    private final String msg;
}
