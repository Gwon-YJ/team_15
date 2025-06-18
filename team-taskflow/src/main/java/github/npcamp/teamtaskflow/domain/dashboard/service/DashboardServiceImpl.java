package github.npcamp.teamtaskflow.domain.dashboard.service;

import github.npcamp.teamtaskflow.domain.common.entity.Task;
import github.npcamp.teamtaskflow.domain.dashboard.dto.response.TaskCompletionResponseDto;
import github.npcamp.teamtaskflow.domain.dashboard.dto.response.TaskStatusResponseDto;
import github.npcamp.teamtaskflow.domain.dashboard.dto.response.TodayMyTaskListResponseDto;
import github.npcamp.teamtaskflow.domain.dashboard.dto.response.TotalTaskResponseDto;
import github.npcamp.teamtaskflow.domain.task.TaskStatus;
import github.npcamp.teamtaskflow.domain.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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


    @Override
    public List<TodayMyTaskListResponseDto> getTodayMyTask(Long userId) {
        //로그인한 사용자 id 조회

        //오늘 날짜
        LocalDate today = LocalDate.now();

        //조회할 상태 리스트
        List<TaskStatus> statusList = Arrays.asList(TaskStatus.TODO, TaskStatus.IN_PROGRESS);

        //조건에 맞는 태스크 조회
        List<Task> tasks = taskRepository.findByAssignee_IdAndDueDateAndStatusInOrderByPriorityDesc(userId,today.atStartOfDay(), statusList);

        return tasks.stream()
                .map(TodayMyTaskListResponseDto::toDto)
                .collect(Collectors.toList());
    }
}
