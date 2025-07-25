package github.npcamp.teamtaskflow.domain.task.dto.response;

import github.npcamp.teamtaskflow.domain.common.base.Identifiable;
import github.npcamp.teamtaskflow.domain.common.entity.Task;
import github.npcamp.teamtaskflow.domain.task.TaskPriority;
import github.npcamp.teamtaskflow.domain.task.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class CreateTaskResponseDto implements Identifiable {

    private final Long id;
    private final String title;
    private final String content;
    private final TaskPriority priority;
    private final TaskStatus status;
    private final String assignee;
    private final LocalDate dueDate;
    private final LocalDateTime createdAt;

    public static CreateTaskResponseDto toDto(Task task) {
        return CreateTaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .content(task.getDescription())
                .priority(task.getPriority())
                .status(task.getStatus())
                .assignee(task.getAssignee().getUsername())
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .build();
    }

    @Override
    public Long getId() {
        return this.id;
    }

}
