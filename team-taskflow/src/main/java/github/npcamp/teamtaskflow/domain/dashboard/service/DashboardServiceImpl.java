package github.npcamp.teamtaskflow.domain.dashboard.service;

import github.npcamp.teamtaskflow.domain.dashboard.dto.response.TotalTaskResponseDto;
import github.npcamp.teamtaskflow.domain.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class DashboardServiceImpl implements DashboardService{

    private final TaskRepository taskRepository;

    @Override
    public TotalTaskResponseDto getTotalTasks() {
        long totalTasks = taskRepository.countByIsDeletedFalse();
        return new TotalTaskResponseDto(totalTasks);
    }
}
