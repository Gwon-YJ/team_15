package github.npcamp.teamtaskflow.domain.auth.service;



import github.npcamp.teamtaskflow.domain.auth.dto.request.LoginRequestDto;
import github.npcamp.teamtaskflow.domain.auth.dto.request.SignUpRequestDto;
import github.npcamp.teamtaskflow.domain.auth.repository.AuthRepository;
import github.npcamp.teamtaskflow.domain.common.entity.User;
import github.npcamp.teamtaskflow.domain.common.enums.UserRoleEnum;
import github.npcamp.teamtaskflow.domain.user.dto.response.UserResponseDto;
import github.npcamp.teamtaskflow.global.exception.CustomException;
import github.npcamp.teamtaskflow.global.exception.ErrorCode;
import github.npcamp.teamtaskflow.global.utils.JwtUtil;
import github.npcamp.teamtaskflow.global.utils.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public String login(LoginRequestDto loginRequestDto) {
        String userName = loginRequestDto.getUserName();
        String password = loginRequestDto.getPassword();

        User user = (User) authRepository.findByUserName(userName)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_EMAIL_MISMATCH);
        }

        return jwtUtil.generateToken(user.getUserName(), user.getRole());
    }

    public UserResponseDto signUp(SignUpRequestDto signUpRequestDto) {
        // 사용자 이름 중복 확인
        if (authRepository.findByUserName(signUpRequestDto.getUserName()).isPresent()) {
            throw new CustomException(ErrorCode.RESOURCE_ALREADY_EXIST);
        }

        User user = new User(
                signUpRequestDto.getName(),               // 이름
                signUpRequestDto.getUserName(),           // 사용자명
                passwordEncoder.encode(signUpRequestDto.getPassword()), // 암호화된 비밀번호
                signUpRequestDto.getEmail(),              // 이메일
                UserRoleEnum.USER                         // 권한
        );

        User savedUser = authRepository.save(user);

        // 저장된 사용자 정보를 바탕으로 응답 DTO 생성
        return new UserResponseDto(savedUser);
    }


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
