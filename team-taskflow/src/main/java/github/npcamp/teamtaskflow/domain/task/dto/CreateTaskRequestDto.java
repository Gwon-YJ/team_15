package github.npcamp.teamtaskflow.domain.task.dto;

import github.npcamp.teamtaskflow.domain.task.TaskPriority;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CreateTaskRequestDto {

    @NotBlank(message = "제목은 필수입니다.")
    private final String title;

    @NotBlank(message = "내용은 필수입니다.")
    private final String content;

    @NotNull(message = "우선순위는 필수입니다.")
    private final TaskPriority priority; // Enum 타입으로 명확히 정의

    @NotNull(message = "담당자 ID는 필수입니다.")
    private final Long assigneeId;

    @NotNull(message = "마감일은 필수입니다.")
    @Future(message = "마감일은 현재 시간보다 이후여야 합니다.")
    private final LocalDateTime dueDate;
}
