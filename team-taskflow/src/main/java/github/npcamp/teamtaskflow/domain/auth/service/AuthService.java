package github.npcamp.teamtaskflow.domain.auth.service;

import github.npcamp.teamtaskflow.domain.auth.dto.request.LoginRequestDto;
import github.npcamp.teamtaskflow.domain.auth.dto.request.RegisterRequestDto;
import github.npcamp.teamtaskflow.domain.auth.dto.request.WithdrawRequestDto;
import github.npcamp.teamtaskflow.domain.auth.dto.response.AuthResponseDto;
import github.npcamp.teamtaskflow.domain.auth.exception.AuthException;
import github.npcamp.teamtaskflow.domain.auth.utils.JwtUtil;
import github.npcamp.teamtaskflow.domain.common.entity.User;
import github.npcamp.teamtaskflow.domain.user.dto.response.UserResponseDto;
import github.npcamp.teamtaskflow.domain.user.exception.UserException;
import github.npcamp.teamtaskflow.domain.user.repository.UserRepository;
import github.npcamp.teamtaskflow.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthResponseDto register(RegisterRequestDto req) {

        // 유니크 이메일 중복 확인
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new AuthException(ErrorCode.DUPLICATE_EMAIL);
        }

        // 유니크 유저네임 중복 확인
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new AuthException(ErrorCode.DUPLICATE_USERNAME); // 예외 발생
        }

        User user = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .name(req.getName())
                .build();

        User savedUser = userRepository.save(user);

        return generateAuthResponse(savedUser);
    }

    @Transactional
    public AuthResponseDto login(LoginRequestDto req) {

        // 사용자 조회
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        //비밀번호 검증
        passwordMatch(req.getPassword(), user.getPassword());

        // JWT 토큰 생성 후 응답
        return generateAuthResponse(user);
    }

    private AuthResponseDto generateAuthResponse(User user) {
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        return new AuthResponseDto(token, UserResponseDto.toDto(user));
    }

    @Transactional
    public void withdraw(WithdrawRequestDto req, Long userId){
        //비밀번호 검증


        User user = userRepository.findById(userId)
                .orElseThrow(()-> new AuthException(ErrorCode.USER_NOT_FOUND));

        passwordMatch(req.getPassword(), user.getPassword());
        userRepository.delete(user);
    }


    /*
    password 검증 메서드
    inputPassword = 입력받은 패스워드
    password = 저장되어있는 패스워드
     */
    public void passwordMatch(String inputPassword, String password){
        if (!encoder.matches(inputPassword, password)) {
            throw new AuthException(ErrorCode.PASSWORD_MISMATCH); // 예외 발생
        }
    }
}
