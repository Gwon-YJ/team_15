package github.npcamp.teamtaskflow.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // user
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),

    // task
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시물을 찾을 수 없습니다."),
    INVALID_STATUS_TRANSITION(HttpStatus.BAD_REQUEST, "TODO → IN_PROGRESS → DONE 순서로만 변경할 수 있습니다."),

    // comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다."),
    TASK_COMMENT_MISMATCH(HttpStatus.BAD_REQUEST,"댓글이 해당 할일(task)에 속하지 않습니다");
    // board

    // log

    private final HttpStatus status;
    private final String msg;
}
