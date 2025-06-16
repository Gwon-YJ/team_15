package github.npcamp.teamtaskflow.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRequestDto {

    @NotBlank(message = "userName은 필수입니다.")
    private final String userName;

    @NotBlank(message = "email은 필수입니다.")
    private final String email;

    @NotBlank(message = "password는 필수입니다.")
    private final String password;

    @NotBlank(message = "savepassword는 필수입니다.")
    private String savePassword;

    @NotBlank(message = "changepassword는 필수입니다.")
    private String changePassword;

}
