package github.npcamp.teamtaskflow.domain.comment.repository;

import github.npcamp.teamtaskflow.domain.common.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @EntityGraph(attributePaths = {"task", "user"})
    Page<Comment> findAllByTaskId(Long taskId, Pageable pageable);
    Page<Comment> findByTaskIdAndContentContainingIgnoreCase(Long taskId, String keyword, Pageable pageable);
}
