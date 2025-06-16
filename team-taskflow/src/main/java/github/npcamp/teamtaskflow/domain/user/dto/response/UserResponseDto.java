package github.npcamp.teamtaskflow.domain.user.dto.response;

import github.npcamp.teamtaskflow.domain.common.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private final Long id;
    private final String name;
    private final String userName;
    private final String email;


    public UserResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName(); // getter 사용
        this.userName = user.getUserName();
        this.email = user.getEmail();

    }
}
