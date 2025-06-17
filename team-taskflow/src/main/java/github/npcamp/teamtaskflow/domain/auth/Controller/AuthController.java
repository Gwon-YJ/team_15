package github.npcamp.teamtaskflow.domain.auth.Controller;

import github.npcamp.teamtaskflow.domain.auth.dto.request.LoginRequestDto;
import github.npcamp.teamtaskflow.domain.auth.dto.request.SignUpRequestDto;
import github.npcamp.teamtaskflow.domain.auth.dto.response.LoginResponseDto;
import github.npcamp.teamtaskflow.domain.auth.service.AuthService;
import github.npcamp.teamtaskflow.domain.user.dto.response.UserResponseDto;
import github.npcamp.teamtaskflow.global.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final AuthService authService; // 의존성 주입

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto loginRequestDto) {
        String token = authService.login(loginRequestDto);
        LoginResponseDto responseDto = new LoginResponseDto(token);
        return ResponseEntity.ok(ApiResponse.success("로그인에 성공하였습니다.", responseDto));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String token) {
        authService.logOut(token.replace("Bearer ", ""));
        return ResponseEntity.ok(ApiResponse.success("로그아웃 되었습니다."));
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDto>> signUp(@Valid @RequestBody SignUpRequestDto dto) {
        UserResponseDto user = authService.signUp(dto);
        return ResponseEntity.ok(ApiResponse.success("회원가입이 완료되었습니다.", user));
    }
}

