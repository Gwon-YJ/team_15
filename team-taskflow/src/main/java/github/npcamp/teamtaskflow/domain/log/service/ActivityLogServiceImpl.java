package github.npcamp.teamtaskflow.domain.log.service;

import github.npcamp.teamtaskflow.domain.common.entity.ActivityLog;
import github.npcamp.teamtaskflow.domain.common.entity.User;
import github.npcamp.teamtaskflow.domain.log.ActivityType;
import github.npcamp.teamtaskflow.domain.log.exception.ActivityLogException;
import github.npcamp.teamtaskflow.domain.log.repository.ActivityLogRepository;
import github.npcamp.teamtaskflow.domain.log.repository.UserRepository;
import github.npcamp.teamtaskflow.domain.task.TaskStatus;
import github.npcamp.teamtaskflow.domain.task.repository.TaskRepository;
import github.npcamp.teamtaskflow.global.exception.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(() -> new EntityNotFoundException("작업이 존재하지 않습니다."))
                .getStatus();
    }

    @Override
    @Transactional
    public void saveActivityLog(Long userId, String ip, String method, String url, ActivityType activityType, Long targetId, String message) {

//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

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

}
