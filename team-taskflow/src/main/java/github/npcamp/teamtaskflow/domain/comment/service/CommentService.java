package github.npcamp.teamtaskflow.domain.comment.service;

import github.npcamp.teamtaskflow.domain.comment.dto.request.CreateCommentRequestDto;
import github.npcamp.teamtaskflow.domain.comment.dto.request.UpdateCommentRequestDto;
import github.npcamp.teamtaskflow.domain.comment.dto.response.CommentDetailDto;
import github.npcamp.teamtaskflow.domain.comment.dto.response.CommentPageDto;
import github.npcamp.teamtaskflow.domain.common.entity.Comment;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    // 댓글 생성
    CommentDetailDto createComment(Long taskId, Long userId, CreateCommentRequestDto requestDto);
    // 댓글 전체 조회
    CommentPageDto getComments(Long taskId, Pageable pageable);
    // 댓글 수정
    CommentDetailDto updateContent(Long taskId, Long commentId, Long userId, UpdateCommentRequestDto requestDto);
    // 댓글 삭제
    void deleteComment(Long taskId, Long commentId, Long userId);
    // commentId로 댓글 조회
    Comment findCommentByIdOrElseThrow(Long commentId);

}
