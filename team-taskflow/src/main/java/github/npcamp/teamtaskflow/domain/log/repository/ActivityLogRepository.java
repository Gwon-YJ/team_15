package github.npcamp.teamtaskflow.domain.log.repository;

import github.npcamp.teamtaskflow.domain.common.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

}
