package github.npcamp.teamtaskflow.domain.task.dto;

import github.npcamp.teamtaskflow.domain.common.entity.Task;
import github.npcamp.teamtaskflow.domain.task.TaskPriority;
import github.npcamp.teamtaskflow.domain.task.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class TaskDetailResponseDto {

    private final Long id;
    private final String title;
    private final String content;
    private final TaskPriority priority;
    private final TaskStatus status;
    private final String assignee;
    private final LocalDateTime dueDate;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static TaskDetailResponseDto toDto(Task task) {
        return TaskDetailResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .content(task.getContent())
                .priority(task.getPriority())
                .status(task.getStatus())
                .assignee(String.valueOf(task.getAssignee()))
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }


}
