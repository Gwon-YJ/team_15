package github.npcamp.teamtaskflow.domain.task.repository;

import github.npcamp.teamtaskflow.domain.common.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
