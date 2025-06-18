package github.npcamp.teamtaskflow.domain.comment.dto.response;

import github.npcamp.teamtaskflow.domain.common.entity.Comment;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record CommentPageDto(
        List<CommentDetailDto> content,
        long totalElements,
        int totalPages,
        int size,
        int number
) {
    public static CommentPageDto toDto(Page<Comment> commentPage) {
        return CommentPageDto.builder()
                .content(commentPage.map(CommentDetailDto::toDto).getContent())
                .totalElements(commentPage.getTotalElements())
                .totalPages(commentPage.getTotalPages())
                .size(commentPage.getSize())
                .number(commentPage.getNumber())
                .build();
    }
}