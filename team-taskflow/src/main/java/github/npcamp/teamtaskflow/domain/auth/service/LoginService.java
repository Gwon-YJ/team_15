package github.npcamp.teamtaskflow.domain.auth.service;

import github.npcamp.teamtaskflow.domain.common.entity.User;
import github.npcamp.teamtaskflow.domain.auth.dto.request.LoginRequestDto;
import github.npcamp.teamtaskflow.domain.auth.repository.LoginRepository;
import github.npcamp.teamtaskflow.global.exception.CustomException;
import github.npcamp.teamtaskflow.global.exception.ErrorCode;
import github.npcamp.teamtaskflow.global.utils.JwtUtil;
import github.npcamp.teamtaskflow.global.utils.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginService {

    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String login(LoginRequestDto loginRequestDto) {
        String userName = loginRequestDto.getUserName();
        String password = loginRequestDto.getPassword();

        User user = loginRepository.findByUserName(userName)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_EMAIL_MISMATCH);
        }

        return jwtUtil.generateToken(user.getUserName(), user.getRole());
    }

}
