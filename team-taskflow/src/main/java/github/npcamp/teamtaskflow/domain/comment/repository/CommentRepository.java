package github.npcamp.teamtaskflow.domain.comment.repository;

import github.npcamp.teamtaskflow.domain.common.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    //TODO: 정렬 조회 등 추가 예정 (comment Id와 task Id 중 하나 선택 예정)
}
