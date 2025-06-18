package github.npcamp.teamtaskflow.domain.log.repository;

import github.npcamp.teamtaskflow.domain.common.entity.ActivityLog;
import github.npcamp.teamtaskflow.domain.log.ActivityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    @Query("SELECT a FROM ActivityLog a " +
            "WHERE (:activityType IS NULL OR a.activityType = :activityType) " +
            "AND (:targetId IS NULL OR a.targetId = :targetId) " +
            "AND (a.timestamp BETWEEN :start AND :end)")
    Page<ActivityLog> findFilteredLogs(
            @Param("activityType") ActivityType activityType,
            @Param("targetId") Long targetId,
            @Param("start") LocalDateTime startDateTime,
            @Param("end") LocalDateTime endDateTime,
            Pageable pageable
    );
}
