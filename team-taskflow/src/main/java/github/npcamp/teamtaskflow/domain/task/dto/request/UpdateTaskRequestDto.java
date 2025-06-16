package github.npcamp.teamtaskflow.domain.task.dto.request;

import github.npcamp.teamtaskflow.domain.task.TaskPriority;
import jakarta.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UpdateTaskRequestDto {

    private String title;
    private String content;
    private TaskPriority priority;
    private Long assigneeId;

    @Future(message = "마감일은 현재 시간보다 이후여야 합니다.")
    private LocalDateTime dueDate;

}