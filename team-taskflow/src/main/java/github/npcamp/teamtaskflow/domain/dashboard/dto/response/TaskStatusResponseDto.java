package github.npcamp.teamtaskflow.domain.dashboard.dto.response;

import github.npcamp.teamtaskflow.domain.task.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
        
//상태별 태스크 개수 조회
public class TaskStatusResponseDto {
    //태스크 상태
    private TaskStatus status;

    //상태별 태스크 수
    private Long count;

}
