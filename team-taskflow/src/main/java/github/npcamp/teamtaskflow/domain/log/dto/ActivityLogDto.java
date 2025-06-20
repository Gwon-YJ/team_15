package github.npcamp.teamtaskflow.domain.log.dto;

import github.npcamp.teamtaskflow.domain.common.entity.ActivityLog;
import github.npcamp.teamtaskflow.domain.log.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ActivityLogDto {

    private final Long id;
    private final String username;
    private final ActivityType activityType;
    private final String changeContent;
    private final Long targetId;
    private final LocalDateTime timestamp;

    public static ActivityLogDto toDto(ActivityLog activityLog){
        return ActivityLogDto.builder()
                .id(activityLog.getId())
                .username(activityLog.getUser().getUsername())
                .activityType(activityLog.getActivityType())
                .changeContent(activityLog.getChangeContent())
                .targetId(activityLog.getTargetId())
                .timestamp(activityLog.getTimestamp())
                .build();
    }

}
