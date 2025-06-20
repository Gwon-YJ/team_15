package github.npcamp.teamtaskflow.domain.task.dto.response;

import github.npcamp.teamtaskflow.domain.common.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * TaskDetailResponseDto에 추가하기 위한 DTO
 */
@Getter
@AllArgsConstructor
@Builder
public class UserResponse {

    private final Long id;
    private final String username;
    private final String name;
    private final String email;

    public static UserResponse toDto(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
