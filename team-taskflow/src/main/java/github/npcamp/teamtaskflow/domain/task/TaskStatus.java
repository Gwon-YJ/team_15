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

}