package github.npcamp.teamtaskflow.domain.auth.dto.request;

import github.npcamp.teamtaskflow.domain.common.enums.UserRoleEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
        @NotNull(message = "role 필수값")
        private final UserRoleEnum role;
    }
