package github.npcamp.teamtaskflow.domain.dashboard.dto.response;

import github.npcamp.teamtaskflow.domain.common.entity.Task;
import github.npcamp.teamtaskflow.domain.task.TaskPriority;
import github.npcamp.teamtaskflow.domain.task.TaskStatus;
import github.npcamp.teamtaskflow.domain.task.dto.response.TaskResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class TodayMyTaskListResponseDto {
    private Long taskId;
    private String title;
    private TaskStatus status;
    private TaskPriority priority;

    public static TodayMyTaskListResponseDto toDto(Task task) {
        return TodayMyTaskListResponseDto.builder()
                .taskId(task.getId())
                .title(task.getTitle())
                .status(task.getStatus())
                .priority(task.getPriority())
                .build();
    }
}
