package github.npcamp.teamtaskflow.domain.user.dto;

import github.npcamp.teamtaskflow.domain.common.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponseDto {
    private final Long id;
    private final String userId;
    private final String userName;
    private final String email;


    public UserResponseDto(User user) {
        this.id = user.getId();
        this.userId = user.getUserId(); // getter 사용
        this.userName = user.getUserName();
        this.email = user.getEmail();

    }
}
