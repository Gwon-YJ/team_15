package github.npcamp.teamtaskflow.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LogOutRequestDto {
    private final String token;  // 로그아웃할 JWT 토큰 등
}
