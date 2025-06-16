package github.npcamp.teamtaskflow.domain.comment.service;

import github.npcamp.teamtaskflow.domain.comment.dto.CreateCommentRequestDto;
import github.npcamp.teamtaskflow.domain.comment.dto.CreateCommentResponseDto;
import github.npcamp.teamtaskflow.domain.common.entity.Comment;

public interface CommentService {
    CreateCommentResponseDto createComment(Long taskId, Long userId, CreateCommentRequestDto requestDto);
    Comment findByIdOrElseThrow(Long commentId);
}
