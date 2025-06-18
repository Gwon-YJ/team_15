package github.npcamp.teamtaskflow.domain.auth.controller;

import github.npcamp.teamtaskflow.domain.auth.dto.request.LoginRequestDto;
import github.npcamp.teamtaskflow.domain.auth.dto.request.RegisterRequestDto;
import github.npcamp.teamtaskflow.domain.auth.dto.response.AuthResponseDto;
import github.npcamp.teamtaskflow.domain.auth.service.AuthService;
import github.npcamp.teamtaskflow.domain.common.aop.Logging;
import github.npcamp.teamtaskflow.domain.log.ActivityType;
import github.npcamp.teamtaskflow.global.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
@Slf4j
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponseDto>> register(@Valid @RequestBody RegisterRequestDto req) {
        AuthResponseDto res = authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(res));
    }

    // 로그인
    @PostMapping("/login")
    @Logging(ActivityType.USER_LOGGED_IN)
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(@Valid @RequestBody LoginRequestDto req) {
        AuthResponseDto res = authService.login(req);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(res));
    }

}
