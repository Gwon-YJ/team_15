package github.npcamp.teamtaskflow.domain.dashboard.service;

import github.npcamp.teamtaskflow.domain.dashboard.dto.response.TaskCompletionResponseDto;
import github.npcamp.teamtaskflow.domain.dashboard.dto.response.TaskStatusResponseDto;
import github.npcamp.teamtaskflow.domain.dashboard.dto.response.TodayMyTaskListResponseDto;
import github.npcamp.teamtaskflow.domain.dashboard.dto.response.TotalTaskResponseDto;

import java.util.List;

public interface DashboardService {
    //전체 태스크 수 조회
    TotalTaskResponseDto getTotalTasks();
    
    //상태별 태스크 수 조회
    List<TaskStatusResponseDto> getStatusTasks();

    //완료율 조회
    TaskCompletionResponseDto getCompletion();

    //오늘 나의 태스크 목록 조회
    List<TodayMyTaskListResponseDto> getTodayMyTask(Long userId);

}
