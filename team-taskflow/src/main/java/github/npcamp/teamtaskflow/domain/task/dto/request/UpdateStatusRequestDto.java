package github.npcamp.teamtaskflow.domain.task.dto.request;

import github.npcamp.teamtaskflow.domain.task.TaskStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateStatusRequestDto {

    @NotNull(message = "상태는 필수입니다.")
    private TaskStatus newStatus;

}