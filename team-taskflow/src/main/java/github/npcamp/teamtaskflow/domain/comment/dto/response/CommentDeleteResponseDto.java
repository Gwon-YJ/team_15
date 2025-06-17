package github.npcamp.teamtaskflow.domain.comment.dto.response;


import github.npcamp.teamtaskflow.domain.common.entity.Comment;
import lombok.Builder;

@Builder
public record CommentDeleteResponseDto(
        Long id,
        Long taskId,
        boolean isDeleted) {
    public static CommentDeleteResponseDto toDto(Comment comment) {
        return CommentDeleteResponseDto.builder()
                .id(comment.getId())
                .taskId(comment.getTask().getId())
                .isDeleted(comment.isDeleted())
                .build();
    }
}
