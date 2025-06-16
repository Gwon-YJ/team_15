package github.npcamp.teamtaskflow.domain.dashboard.service;

import github.npcamp.teamtaskflow.domain.dashboard.dto.response.TaskStatusResponseDto;
import github.npcamp.teamtaskflow.domain.dashboard.dto.response.TotalTaskResponseDto;
import github.npcamp.teamtaskflow.domain.task.TaskStatus;
import github.npcamp.teamtaskflow.domain.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor

public class DashboardServiceImpl implements DashboardService{

    private final TaskRepository taskRepository;

    @Override
    public TotalTaskResponseDto getTotalTasks() {
        long totalTasks = taskRepository.countByIsDeletedFalse();
        return new TotalTaskResponseDto(totalTasks);
    }

    @Override
    public List<TaskStatusResponseDto> getStatusTasks() {
        List<Object[]> groupList = taskRepository.countGroupByStatus();
        Map<TaskStatus,Long> statusTasksCount = new EnumMap<>(TaskStatus.class);

        //결과 저장
        for(Object[] row : groupList){
            TaskStatus status=(TaskStatus) row[0];
            Long count=(Long)row[1];
            statusTasksCount.put(status,count);
        }

        //DTO 리스트로 변환
        List<TaskStatusResponseDto> responseDtoList = new ArrayList<>();

        for(TaskStatus status: TaskStatus.values()){
            long count=statusTasksCount.getOrDefault(status,0L);
            responseDtoList.add(new TaskStatusResponseDto(status,count));
        }
        return responseDtoList;
    }
}
