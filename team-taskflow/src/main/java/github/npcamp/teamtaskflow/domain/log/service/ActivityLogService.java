package github.npcamp.teamtaskflow.domain.log.service;

import github.npcamp.teamtaskflow.domain.common.entity.ActivityLog;
import github.npcamp.teamtaskflow.domain.log.ActivityType;
import github.npcamp.teamtaskflow.domain.log.dto.ActivityLogDto;
import github.npcamp.teamtaskflow.domain.log.repository.ActivityLogRepository;
import github.npcamp.teamtaskflow.domain.task.TaskStatus;
import github.npcamp.teamtaskflow.global.payload.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ActivityLogService {

    TaskStatus findTaskStatus(Long taskId);

    void saveActivityLog(Long userId, String ip, String method, String url, ActivityType activityType, Long targetId, String message);

    PageResponse<ActivityLogDto> getActivitiesLog(ActivityType activityType, Long targetId, LocalDate start, LocalDate end, Pageable pageable);

}
