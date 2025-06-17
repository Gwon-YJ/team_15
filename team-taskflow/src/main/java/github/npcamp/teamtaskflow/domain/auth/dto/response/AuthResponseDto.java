package github.npcamp.teamtaskflow.domain.auth.dto.response;

import github.npcamp.teamtaskflow.domain.user.dto.response.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponseDto {

    private String token;
    private UserResponseDto user;

}
