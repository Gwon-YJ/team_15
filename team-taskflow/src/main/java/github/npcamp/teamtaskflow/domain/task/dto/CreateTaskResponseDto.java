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
public class CreateTaskResponseDto {

    private final Long id;
    private final String title;
    private final String content;
    private final TaskPriority priority;
    private final TaskStatus status;
    private final String assignee;
    private final LocalDateTime dueDate;
    private final LocalDateTime createdAt;

    public static CreateTaskResponseDto toDto(Task task) {
        return CreateTaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .content(task.getContent())
                .priority(task.getPriority())
                .status(task.getStatus())
                .assignee(task.getAssignee().getUserName())
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .build();
    }

}
