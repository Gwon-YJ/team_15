package github.npcamp.teamtaskflow.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateCommentRequestDto(
        @NotBlank(message = "댓글 내용은 비어있을 수 없습니다.")
        String content
) {
}