package github.npcamp.teamtaskflow.domain.dashboard.service;

import github.npcamp.teamtaskflow.domain.dashboard.dto.response.TaskStatusResponseDto;
import github.npcamp.teamtaskflow.domain.dashboard.dto.response.TotalTaskResponseDto;
import github.npcamp.teamtaskflow.domain.task.TaskStatus;
import github.npcamp.teamtaskflow.domain.task.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceImplTest {

    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    DashboardServiceImpl dashboardService;

    @Test
    void 대시보드_전체_태스크_수_조회_테스트() {
        // given
        when(taskRepository.countByIsDeletedFalse()).thenReturn(3L);

        // when
        TotalTaskResponseDto result = dashboardService.getTotalTasks();

        // then
        assertThat(result.getTotalTasks()).isEqualTo(3L);
        verify(taskRepository).countByIsDeletedFalse();
    }

    @Test
    void 대시보드_상태별_태스크_수_조회_테스트_TODO() {
        // given
        // 테스트용 상태별 카운트 리스트
       List<Object[]> groupList = Arrays.asList(
         new Object[]{TaskStatus.TODO,5L},
         new Object[]{TaskStatus.IN_PROGRESS,3L},
         new Object[]{TaskStatus.DONE,2L}
       );
       //mock taskRepository에서 countGroupByStatus가 groupList를 반환.
        when(taskRepository.countGroupByStatus()).thenReturn(groupList);

        // when
        //서비스 메서드 호출
        List<TaskStatusResponseDto> list = dashboardService.getStatusTasks();

        // then
        //반환된 list에서 filter로 TaskStatus.TODO값 꺼냄.
        TaskStatusResponseDto todoDto= list.stream()
                        .filter(dto->dto.getStatus()==TaskStatus.TODO)
                        .findFirst()
                        .orElseThrow();

        //then
        //꺼낸 TODO상태의 값이 5와 같은지 비교.
        assertThat(todoDto.getCount()).isEqualTo(5L);
    }

    @Test
    void 대시보드_상태별_태스크_수_조회_테스트_IN_PROGRESS() {
        // given
        // 테스트용 상태별 카운트 리스트
        List<Object[]> groupList = Arrays.asList(
                new Object[]{TaskStatus.TODO,5L},
                new Object[]{TaskStatus.IN_PROGRESS,3L},
                new Object[]{TaskStatus.DONE,2L}
        );
        //mock taskRepository에서 countGroupByStatus가 groupList를 반환.
        when(taskRepository.countGroupByStatus()).thenReturn(groupList);

        // when
        //서비스 메서드 호출
        List<TaskStatusResponseDto> list = dashboardService.getStatusTasks();

        // then
        //반환된 list에서 filter로 TaskStatus.IN_PROGRESS값 꺼냄.
        TaskStatusResponseDto todoDto= list.stream()
                .filter(dto->dto.getStatus()==TaskStatus.IN_PROGRESS)
                .findFirst()
                .orElseThrow();

        //then
        //꺼낸 IN_PROGRESS상태의 값이 3과 같은지 비교.
        assertThat(todoDto.getCount()).isEqualTo(3L);
    }

    @Test
    void 대시보드_상태별_태스크_수_조회_테스트_DONE() {
        // given
        // 테스트용 상태별 카운트 리스트
        List<Object[]> groupList = Arrays.asList(
                new Object[]{TaskStatus.TODO,5L},
                new Object[]{TaskStatus.IN_PROGRESS,3L},
                new Object[]{TaskStatus.DONE,2L}
        );
        //mock taskRepository에서 countGroupByStatus가 groupList를 반환.
        when(taskRepository.countGroupByStatus()).thenReturn(groupList);

        // when
        //서비스 메서드 호출
        List<TaskStatusResponseDto> list = dashboardService.getStatusTasks();

        // then
        //반환된 list에서 filter로 TaskStatus.DONE 꺼냄.
        TaskStatusResponseDto todoDto= list.stream()
                .filter(dto->dto.getStatus()==TaskStatus.DONE)
                .findFirst()
                .orElseThrow();

        //then
        //꺼낸 DONE상태의 값이 2과 같은지 비교.
        assertThat(todoDto.getCount()).isEqualTo(2L);
    }

}
