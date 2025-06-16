package github.npcamp.teamtaskflow.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateCommentRequestDto(
        @NotBlank(message = "댓글 내용은 비어있을 수 없습니다.")
        String content,
        @NotBlank(message = "작성자 ID는 비어있을 수 없습니다.")
        String username // 이게 식별자 역할 (PK처럼 동작)
) {
}
