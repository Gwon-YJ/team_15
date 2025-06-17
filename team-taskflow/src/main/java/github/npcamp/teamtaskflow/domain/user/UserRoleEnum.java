package github.npcamp.teamtaskflow.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRoleEnum {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String role;        // Spring Security 권한 이름

}