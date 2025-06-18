package github.npcamp.teamtaskflow.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 공통
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예상하지 못한 에러"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근권한이 없습니다."),

    // JwtFilter
    NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."), // 필터에서 토큰 유효성 검증에 사용
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인 해주세요!"),

    // auth
    PASSWORD_MISMATCH(HttpStatus.FORBIDDEN, "비밀번호가 일치하지 않습니다"),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 사용자명입니다."),

    // user
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회원을 찾을 수 없습니다."),

    // task
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시물을 찾을 수 없습니다."),
    INVALID_STATUS_TRANSITION(HttpStatus.BAD_REQUEST, "TODO → IN_PROGRESS → DONE 순서로만 변경할 수 있습니다."),

    // comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다."),
    TASK_COMMENT_MISMATCH(HttpStatus.BAD_REQUEST,"댓글이 해당 할일(task)에 속하지 않습니다"),
    UNAUTHORIZED_COMMENT_ACCESS(HttpStatus.FORBIDDEN, "본인의 댓글만 수정 또는 삭제할 수 있습니다."),

    // board

    // log
    ACTIVITY_LOG_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "활동 로그 저장 중 오류가 발생했습니다."),
    INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "시작일은 종료일보다 이전이어야 합니다.");



    private final HttpStatus status;
    private final String msg;
}
