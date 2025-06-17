package github.npcamp.teamtaskflow.domain.dashboard.service;

import github.npcamp.teamtaskflow.domain.dashboard.dto.response.TaskCompletionResponseDto;
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

    //전체 태스크 조회
    @Override
    public TotalTaskResponseDto getTotalTasks() {
        long totalTasks = taskRepository.countByIsDeletedFalse();
        return new TotalTaskResponseDto(totalTasks);
    }

    //상태별 태스크 조회
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


    //전체 태스크 대비 완료율 조회
    @Override
    public TaskCompletionResponseDto getCompletion() {
        //DONE 상태의 태스크 수
        long doneCount=taskRepository.countByStatusAndIsDeletedFalse(TaskStatus.DONE);

        //전체 태스크 수
        long totalCount=taskRepository.countByIsDeletedFalse();

        //전체 카운트가 0인 경우 완료율 0.0 출력
        if(totalCount==0) return new TaskCompletionResponseDto(0.0);

        double completionRate = ((double)doneCount/totalCount)*100;

        double round=Math.round(completionRate*100.0)/100.0;

        return new TaskCompletionResponseDto(round);
    }
}
