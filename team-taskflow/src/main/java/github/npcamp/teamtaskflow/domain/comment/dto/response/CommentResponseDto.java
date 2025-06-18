package github.npcamp.teamtaskflow.domain.comment.dto.response;

import github.npcamp.teamtaskflow.domain.common.base.Identifiable;
import github.npcamp.teamtaskflow.domain.common.entity.Comment;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentResponseDto(
        Long id,
        Long taskId,
        Long userId,
        String username,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean isDeleted
) implements Identifiable {

    public static CommentResponseDto toDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .taskId(comment.getTask().getId())
                .userId(comment.getUser().getId())
                .username(comment.getUser().getUsername())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .isDeleted(comment.isDeleted())
                .build();
    }

    @Override
    public Long getId() {
        return this.id;
    }
}


