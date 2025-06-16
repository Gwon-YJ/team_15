package github.npcamp.teamtaskflow.domain.dashboard;

import github.npcamp.teamtaskflow.domain.dashboard.dto.response.TotalTaskResponseDto;
import github.npcamp.teamtaskflow.domain.dashboard.service.DashboardServiceImpl;
import github.npcamp.teamtaskflow.domain.task.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

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
}
