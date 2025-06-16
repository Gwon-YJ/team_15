package github.npcamp.teamtaskflow.domain.auth.service;

import github.npcamp.teamtaskflow.global.exception.CustomException;
import github.npcamp.teamtaskflow.global.exception.ErrorCode;
import github.npcamp.teamtaskflow.global.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class LogOutService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

    // 로그아웃 (토큰 블랙리스트 등록)
    public void logOut(String authorizationHeader) {
        String token = jwtUtil.extractBearerToken(authorizationHeader);

        if (!jwtUtil.validateToken(token)) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        }

        long expirationSeconds = jwtUtil.getExpiration(token); // 초 단위로 받는다고 가정
        long expirationMillis = expirationSeconds * 1000;

        redisTemplate.opsForValue().set(token, "logout", expirationMillis, TimeUnit.MILLISECONDS);
    }

    // 블랙리스트 확인
    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }
}
