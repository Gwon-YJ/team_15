package github.npcamp.teamtaskflow.domain.comment.dto;

import github.npcamp.teamtaskflow.domain.common.entity.Comment;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateCommentResponseDto (
        Long id,
        Long taskId,
        Long userId,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean isDeleted
) {
    public static CreateCommentResponseDto toDto(Comment comment) {
        return CreateCommentResponseDto.builder()
                .id(comment.getId())
                .taskId(comment.getTask().getId())
                .userId(comment.getUser().getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .isDeleted(comment.isDeleted())
                .build();
    }
}
