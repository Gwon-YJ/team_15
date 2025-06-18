package github.npcamp.teamtaskflow.domain.comment.service;

import github.npcamp.teamtaskflow.domain.comment.dto.request.CreateCommentRequestDto;
import github.npcamp.teamtaskflow.domain.comment.dto.request.UpdateCommentRequestDto;
import github.npcamp.teamtaskflow.domain.comment.dto.response.CommentDeleteResponseDto;
import github.npcamp.teamtaskflow.domain.comment.dto.response.CommentResponseDto;
import github.npcamp.teamtaskflow.domain.comment.dto.response.CommentResponseListDto;
import github.npcamp.teamtaskflow.domain.common.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    // 댓글 생성
    CommentResponseDto createComment(Long taskId, Long userId, CreateCommentRequestDto requestDto);
    // 댓글 전체 조회
    Page<CommentResponseListDto> getComments(Long taskId, Pageable pageable);
    // 댓글 수정
    CommentResponseDto updateContent(Long taskId, Long commentId, Long userId, UpdateCommentRequestDto requestDto);
    // 댓글 삭제
    CommentDeleteResponseDto deleteComment(Long taskId, Long commentId, Long userId);
    // commentId로 댓글 조회
    Comment findCommentByIdOrElseThrow(Long commentId);

}
