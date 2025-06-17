package github.npcamp.teamtaskflow.domain.auth.dto.request;

import github.npcamp.teamtaskflow.domain.user.UserRoleEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterRequestDto {

    @Email(message = "유효한 이메일 형식이 아닙니다")
    @NotBlank(message = "이메일은 필수입니다")
    private String email;

    @Size(min = 2, max = 50, message = "이름은 2-50자 사이여야 합니다")
    @NotBlank(message = "이름은 필수입니다")
    private String name;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 8자 이상의 영문/숫자/특수문자 조합이어야 합니다")
    @NotBlank(message = "비밀번호는 필수입니다")
    private String password;

    @NotBlank(message = "사용자명은 필수입니다")
    private String username;
}
