package github.npcamp.teamtaskflow.domain.auth.dto.request;

import github.npcamp.teamtaskflow.domain.common.enums.UserRoleEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SignUpRequestDto {
        @NotBlank(message = "userName 필수값")
        private final String userName;
        @NotBlank(message = "name 필수값")
        private final String name;
        @NotBlank(message = "email 필수값")
        private final String email;
        @NotBlank(message = "password 필수값")
        private final String password;

}
