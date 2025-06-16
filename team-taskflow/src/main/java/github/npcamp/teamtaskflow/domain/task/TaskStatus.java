package github.npcamp.teamtaskflow.domain.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TaskStatus {

    TODO("할 일"),
    IN_PROGRESS("진행 중"),
    DONE("완료");

    private final String status;

    /**
     * 현재 상태(this)에서 target 상태로 전환 가능한지 검사
     */
    public boolean canTransitionTo(TaskStatus target) {
        return (this == TODO && target == IN_PROGRESS) || (this == IN_PROGRESS && target == DONE);
    }
}