package github.npcamp.teamtaskflow.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRequestDto {
    @Getter
    @AllArgsConstructor
    public static class SignUp{
        @NotBlank
        private final String userName;
        @NotBlank
        private final String UserId;
        @NotBlank
        private final String email;
        @NotBlank
        private final String password;
    }

    @Getter
    @AllArgsConstructor
    public static class FindByName{
        @NotBlank
        private final String userName;
    }

    @Getter
    @AllArgsConstructor
    public static class UpdateUser{
        private final String userName;
        private final String email;

        //본인 확인용
        @NotBlank
        private final String password;
    }

    @Getter
    @AllArgsConstructor
    public static class DeleteUser{
        @NotBlank
        private final String password;
    }

    @Getter
    @AllArgsConstructor
    public static class UpdatePw{
        @NotBlank
        private final String savePassword;
        @NotBlank
        private final String changePassword;
    }

    @Getter
    @AllArgsConstructor
    public static class Login{
        @NotBlank
        private final String email;
        @NotBlank
        private final String password;
    }
}
