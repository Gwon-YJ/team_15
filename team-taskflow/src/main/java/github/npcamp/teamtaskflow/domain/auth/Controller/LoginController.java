package github.npcamp.teamtaskflow.domain.auth.Controller;

import github.npcamp.teamtaskflow.domain.auth.dto.request.LoginRequestDto;
import github.npcamp.teamtaskflow.domain.auth.dto.response.LoginResponseDto;
import github.npcamp.teamtaskflow.domain.auth.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
    @RequestMapping("/api")
    @RestController
    public class LoginController {

        private final LoginService loginService;

        // 로그인
        @PostMapping("/login")
        public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
            String token = loginService.login(loginRequestDto);
            return ResponseEntity.ok(new LoginResponseDto(token));
        }

}
