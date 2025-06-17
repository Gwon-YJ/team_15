package github.npcamp.teamtaskflow.domain.dashboard.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TaskCompletionResponseDto {

    //완료율
    private Double completionRate;
}
