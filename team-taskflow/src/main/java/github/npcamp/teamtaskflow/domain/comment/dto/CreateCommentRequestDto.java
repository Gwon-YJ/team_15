package github.npcamp.teamtaskflow.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCommentRequestDto(
        @NotBlank(message = "댓글내용은 비어있을 수 없습니다.")
        String content) { }
