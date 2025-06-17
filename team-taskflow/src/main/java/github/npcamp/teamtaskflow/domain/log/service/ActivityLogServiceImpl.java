package github.npcamp.teamtaskflow.domain.log.service;

import github.npcamp.teamtaskflow.domain.common.entity.ActivityLog;
import github.npcamp.teamtaskflow.domain.common.entity.User;
import github.npcamp.teamtaskflow.domain.log.ActivityType;
import github.npcamp.teamtaskflow.domain.log.dto.ActivityLogDto;
import github.npcamp.teamtaskflow.domain.log.exception.ActivityLogException;
import github.npcamp.teamtaskflow.domain.log.repository.ActivityLogRepository;
import github.npcamp.teamtaskflow.domain.task.TaskStatus;
import github.npcamp.teamtaskflow.domain.task.exception.TaskException;
import github.npcamp.teamtaskflow.domain.task.repository.TaskRepository;
import github.npcamp.teamtaskflow.domain.user.exception.UserException;
import github.npcamp.teamtaskflow.domain.user.repository.UserRepository;
import github.npcamp.teamtaskflow.global.exception.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActivityLogServiceImpl implements ActivityLogService{

    private final ActivityLogRepository activityLogRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Override
    public TaskStatus findTaskStatus(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskException(ErrorCode.TASK_NOT_FOUND))
                .getStatus();
    }

    @Override
    @Transactional
    public void saveActivityLog(Long userId, String ip, String method, String url, ActivityType activityType, Long targetId, String message) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        try {
            ActivityLog activityLog = ActivityLog.builder()
                    .user(user)
                    .ipAddress(ip)
                    .requestMethod(method)
                    .requestUrl(url)
                    .activityType(activityType)
                    .targetId(targetId)
                    .timestamp(LocalDateTime.now())
                    .changeContent(message)
                    .build();

            activityLogRepository.save(activityLog);
        } catch (Exception e) {
            throw new ActivityLogException(ErrorCode.ACTIVITY_LOG_SAVE_FAILED);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityLogDto> getActivitiesLog(ActivityType activityType,
                                                 Long targetId,
                                                 LocalDate startDate,
                                                 LocalDate endDate,
                                                 Pageable pageable) {

        // 시작일이 null이면 2000년 1월 1일 부터
        LocalDateTime startDateTime = (startDate != null)
                ? startDate.atStartOfDay()
                : LocalDate.of(2000, 1, 1).atStartOfDay();

        // 종료일이 null이면 오늘 날짜의 끝으로 설정
        LocalDateTime endDateTime = (endDate != null)
                ? endDate.atTime(23, 59, 59, 999_999_999)
                : LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999_999_999);

        // 날짜 범위가 잘못된 경우
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new ActivityLogException(ErrorCode.INVALID_DATE_RANGE);
        }

        Page<ActivityLog> logs = activityLogRepository.findFilteredLogs(
                activityType, targetId, startDateTime, endDateTime, pageable
        );

        return logs.map(ActivityLogDto::toDto);
    }
}
