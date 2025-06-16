package github.npcamp.teamtaskflow.domain.dashboard.service;

import github.npcamp.teamtaskflow.domain.dashboard.dto.response.TotalTaskResponseDto;

public interface DashboardService {
    TotalTaskResponseDto getTotalTasks();
}
