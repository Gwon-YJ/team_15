package github.npcamp.teamtaskflow.domain.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TaskPriority {

    HIGH("높음"),
    MEDIUM("중간"),
    LOW("낮음");

    private final String priority;

}