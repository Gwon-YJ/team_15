package github.npcamp.teamtaskflow.domain.task.dto.request;

import github.npcamp.teamtaskflow.domain.task.TaskPriority;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CreateTaskRequestDto {

    @NotNull(message = "담당자 ID는 필수입니다.")
    private final Long assigneeId;

    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    private final String title;

    @NotBlank(message = "내용은 필수 입력 항목입니다.")
    private final String description;

    @NotNull(message = "마감일은 필수입니다.")
    @Future(message = "마감일은 현재 시간보다 이후여야 합니다.")
    private final LocalDate dueDate;

    @NotNull(message = "우선순위는 필수입니다.")
    private final TaskPriority priority;

}
