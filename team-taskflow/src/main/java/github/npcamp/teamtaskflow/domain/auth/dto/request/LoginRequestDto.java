package github.npcamp.teamtaskflow.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequestDto {
    @NotBlank(message = "사용자명은 필수입니다")
    private String userName;

    @NotBlank(message = "비밀번호는 필수입니다")
    private String password;
}
