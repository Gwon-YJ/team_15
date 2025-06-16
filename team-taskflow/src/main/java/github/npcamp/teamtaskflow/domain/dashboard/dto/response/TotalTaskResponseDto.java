package github.npcamp.teamtaskflow.domain.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TotalTaskResponseDto {
    //전체 태스크 수
    private long totalTasks;
}
