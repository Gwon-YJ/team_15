package github.npcamp.teamtaskflow.domain.comment.dto.response;


public record CommentDeleteResponseDto(
        Long id,
        Long taskId,
        boolean isDeleted) {
}
