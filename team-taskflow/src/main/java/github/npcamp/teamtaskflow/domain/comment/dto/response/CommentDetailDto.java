package github.npcamp.teamtaskflow.domain.comment.dto.response;

import github.npcamp.teamtaskflow.domain.common.base.Identifiable;
import github.npcamp.teamtaskflow.domain.common.entity.Comment;
import github.npcamp.teamtaskflow.domain.common.entity.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentDetailDto(
        Long id,
        String content,
        Long taskId,
        Long userId,
        UserDto user,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Identifiable {
    @Builder
    public record UserDto(
            Long id,
            String username,
            String name,
            String email
    ) {}

    public static CommentDetailDto toDto(Comment comment) {
        User user = comment.getUser();
        return CommentDetailDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .taskId(comment.getTask().getId())
                .userId(user.getId())
                .user(UserDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .name(user.getName())
                        .email(user.getEmail())
                        .build())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    @Override
    public Long getId(){
        return this.id;
    }
}


