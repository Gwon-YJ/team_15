package github.npcamp.teamtaskflow.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpRequestDto {
        @NotBlank(message = "userName 필수값")
        private final String userName;
        @NotBlank(message = "email 필수값")
        private final String email;
        @NotBlank(message = "password 필수값")
        private final String password;
    }
