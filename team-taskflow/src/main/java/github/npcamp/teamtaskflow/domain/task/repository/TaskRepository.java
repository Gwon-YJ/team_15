package github.npcamp.teamtaskflow.domain.task.repository;

import github.npcamp.teamtaskflow.domain.common.entity.Task;
import github.npcamp.teamtaskflow.domain.task.TaskStatus;
import org.hibernate.annotations.Formula;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String titleKeyword, String contentKeyword, Pageable pageable);

    long countByIsDeletedFalse();

    //상태별로 groupBy
    @Query("SELECT t.status, COUNT(t) FROM Task t GROUP BY t.status")
    List<Object[]> countGroupByStatus();

    //해당되는 상태에 맞게 count
    long countByStatusAndIsDeletedFalse(TaskStatus status);


    /*우선순위를 1,2,3으로 치환한 후 정렬*/
    @Query("SELECT t FROM Task t " +
            "WHERE t.assignee.id = :userId " +
            "AND t.status IN :statusList " +
            "AND t.dueDate >=:dueDate " +
            "ORDER BY " +
            "CASE t.priority " +
            "WHEN 'HIGH' THEN 1 " +
            "WHEN 'MEDIUM' THEN 2 " +
            "WHEN 'LOW' THEN 3 " +
            "END ASC")
    List<Task>  findSortedTasksByPriority(Long userId,List<TaskStatus>statusList, LocalDate dueDate);


}
