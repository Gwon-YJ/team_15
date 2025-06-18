package github.npcamp.teamtaskflow.domain.log.service;

import github.npcamp.teamtaskflow.domain.common.entity.ActivityLog;
import github.npcamp.teamtaskflow.domain.common.entity.User;
import github.npcamp.teamtaskflow.domain.log.ActivityType;
import github.npcamp.teamtaskflow.domain.log.dto.ActivityLogDto;
import github.npcamp.teamtaskflow.domain.log.repository.ActivityLogRepository;
import github.npcamp.teamtaskflow.domain.task.repository.TaskRepository;
import github.npcamp.teamtaskflow.domain.user.UserRoleEnum;
import github.npcamp.teamtaskflow.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import static org.mockito.BDDMockito.given;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ActivityLogServiceImplTest {
    @Mock
    ActivityLogRepository activityLogRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    ActivityLogServiceImpl activityLogService;

    @Test
    void 활동로그_저장(){
        //given
        Long userId = 1L;
        String ip = "127.0.0.1";
        String method = "POST";
        String url = "/tasks";
        ActivityType activityType = ActivityType.TASK_CREATED;
        Long targetId = 1L;
        String message = "작업이 생성되었습니다.";

        User user = User.builder()
                .id(userId)
                .username("test")
                .build();

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        activityLogService.saveActivityLog(userId, ip, method, url, activityType, targetId, message);

        //then
        verify(activityLogRepository, times(1)).save(any(ActivityLog.class));
    }

    @Test
    void 활동로그_정상조회(){
        // given
        User user = User.builder()
                .id(100L)
                .username("test")
                .build();

        ActivityType activityType = ActivityType.TASK_CREATED;
        Long targetId = 1L;
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 31);
        Pageable pageable = PageRequest.of(0, 10);

        ActivityLog activityLog = ActivityLog.builder()
                .id(1L)
                .user(user)
                .activityType(activityType)
                .targetId(targetId)
                .changeContent("테스트 내용")
                .timestamp(LocalDateTime.of(2024, 1, 15, 10, 0))
                .build();

        Page<ActivityLog> resultPage = new PageImpl<>(List.of(activityLog));

        given(activityLogRepository.findFilteredLogs(any(), any(), any(), any(), any()))
                .willReturn(resultPage);

        // when
        Page<ActivityLogDto> result = activityLogService.getActivitiesLog(
                activityType, targetId, start, end, pageable
        );

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getActivityType()).isEqualTo(activityType);
    }
}