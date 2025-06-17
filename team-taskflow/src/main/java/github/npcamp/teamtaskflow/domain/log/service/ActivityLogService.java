package github.npcamp.teamtaskflow.domain.log.service;

import github.npcamp.teamtaskflow.domain.log.ActivityType;
import github.npcamp.teamtaskflow.domain.task.TaskStatus;

public interface ActivityLogService {

    TaskStatus findTaskStatus(Long taskId);

    void saveActivityLog(Long userId, String ip, String method, String url, ActivityType activityType, Long targetId, String message);


}
