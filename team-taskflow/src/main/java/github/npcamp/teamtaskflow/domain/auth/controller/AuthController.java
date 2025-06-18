package github.npcamp.teamtaskflow.domain.auth.controller;

import github.npcamp.teamtaskflow.domain.auth.dto.request.LoginRequestDto;
import github.npcamp.teamtaskflow.domain.auth.dto.request.RegisterRequestDto;
import github.npcamp.teamtaskflow.domain.auth.dto.response.AuthResponseDto;
import github.npcamp.teamtaskflow.domain.auth.service.AuthService;
import github.npcamp.teamtaskflow.global.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponseDto>> register(@Valid @RequestBody RegisterRequestDto req) {
        AuthResponseDto res = authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("회원가입이 완료되었습니다.", res));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(@Valid @RequestBody LoginRequestDto req) {
        AuthResponseDto res = authService.login(req);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("로그인이 완료되었습니다", res));
    }

}
