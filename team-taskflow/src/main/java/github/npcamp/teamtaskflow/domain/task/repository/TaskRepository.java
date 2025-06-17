package github.npcamp.teamtaskflow.domain.task.repository;

import github.npcamp.teamtaskflow.domain.common.entity.Task;
import github.npcamp.teamtaskflow.domain.task.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String titleKeyword, String contentKeyword, Pageable pageable);

    long countByIsDeletedFalse();

    //상태별로 groupBy
    @Query("SELECT t.status, COUNT(t) FROM Task t GROUP BY t.status")
    List<Object[]> countGroupByStatus();

    //해당되는 상태에 맞게 count
    long countByStatusAndIsDeletedFalse(TaskStatus status);
}
