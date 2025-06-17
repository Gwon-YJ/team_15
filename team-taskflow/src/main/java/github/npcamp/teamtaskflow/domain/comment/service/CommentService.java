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
    CommentResponseDto createComment(Long taskId, CreateCommentRequestDto requestDto);
    Comment findByIdOrElseThrow(Long commentId);
    Page<CommentResponseListDto> getComments(Long taskId, Pageable pageable);
    CommentResponseDto updateContent(Long taskId, Long commentId, UpdateCommentRequestDto requestDto);
    CommentDeleteResponseDto deleteComment(Long taskId, Long commentId);
    Page<CommentResponseListDto> searchComments(Long taskId, String keyword, Pageable pageable);
}
