package github.npcamp.teamtaskflow.domain.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TotalTaskResponseDto {
    //전체 태스크 수
    private Long totalTasks;
}
