package github.npcamp.teamtaskflow.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import github.npcamp.teamtaskflow.domain.common.base.Identifiable;
import github.npcamp.teamtaskflow.domain.common.entity.User;
import github.npcamp.teamtaskflow.domain.user.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class UserResponseDto implements Identifiable {
    private Long id;
    private String username;
    private String email;
    private String name;
    private UserRoleEnum role;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;

    public static UserResponseDto toDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }

    @Override
    public Long getId() {
        return this.id;
    }

}