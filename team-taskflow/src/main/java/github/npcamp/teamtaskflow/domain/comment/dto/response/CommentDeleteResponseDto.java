package github.npcamp.teamtaskflow.domain.comment.dto.response;

import java.time.LocalDateTime;

public record CommentDeleteResponseDto(
        Long id,
        Long taskId,
        boolean isDeleted,
        LocalDateTime deleteAt) {
}
