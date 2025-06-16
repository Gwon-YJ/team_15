package github.npcamp.teamtaskflow.domain.dashboard.service;

import github.npcamp.teamtaskflow.domain.dashboard.dto.response.TotalTaskResponseDto;
import github.npcamp.teamtaskflow.domain.dashboard.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class DashboardServiceImpl implements DashboardService{

    private final DashboardRepository dashboardRepository;

    @Override
    public TotalTaskResponseDto getTotalTasks() {
        long totalTasks = dashboardRepository.countAllTasks();
        return new TotalTaskResponseDto(totalTasks);
    }
}
