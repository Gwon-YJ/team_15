package github.npcamp.teamtaskflow.domain.log;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActivityType {
    TASK_CREATED("작업이 생성되었습니다."),
    TASK_UPDATED("작업이 수정되었습니다."),
    TASK_DELETED("작업이 삭제되었습니다."),
    TASK_STATUS_CHANGED("작업 상태가 %s에서 %s(으)로 변경되었습니다."),
    COMMENT_CREATED("댓글이 작성되었습니다."),
    COMMENT_DELETED("댓글이 삭제되었습니다."),
    USER_LOGGED_IN("로그인하였습니다."),
    USER_LOGGED_OUT("로그아웃하였습니다.");


    private final String type;

}
