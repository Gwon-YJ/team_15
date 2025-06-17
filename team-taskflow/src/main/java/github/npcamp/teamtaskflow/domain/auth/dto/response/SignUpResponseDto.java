package github.npcamp.teamtaskflow.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class SignUpResponseDto {
    private String username;
    private String email;
    private String password;
}