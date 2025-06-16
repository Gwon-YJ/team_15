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

    // comment

    // board

    // log

    // DB 조회 실패
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "데이터를 찾을 수 없습니다."),//DB 조회 실패
    // @VALID 실패
    METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "형식을 준수해서 입력해야 합니다."),
    // 인가 실패
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    // 인증 실패: 로그인 시 비밀번호, 이메일 불일치
    PASSWORD_EMAIL_MISMATCH(HttpStatus.UNAUTHORIZED, "비밀번호나 이메일이 일치하지 않습니다."),
    // 토큰 만료
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    // 비밀번호 변경 시 불일치
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    // 리소스 중복
    RESOURCE_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 등록된 이메일입니다.");

    private final HttpStatus status;
    private final String msg;

    }
