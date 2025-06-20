package github.npcamp.teamtaskflow.domain.comment.repository;

import github.npcamp.teamtaskflow.domain.common.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // task에 달린 댓글들을 페이징하여 조회
    @EntityGraph(attributePaths = {"task", "user"})
    Page<Comment> findAllByTaskId(Long taskId, Pageable pageable);

    // 전체 댓글 중 keyword가 포함된 댓글 조회
    @EntityGraph(attributePaths = {"task", "user"})
    Page<Comment> findByContentContainingIgnoreCase(String keyword, Pageable pageable);
}
