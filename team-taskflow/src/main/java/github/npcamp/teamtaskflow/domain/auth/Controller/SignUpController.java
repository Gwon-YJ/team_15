package github.npcamp.teamtaskflow.domain.auth.Controller;

import github.npcamp.teamtaskflow.domain.auth.dto.request.SignUpRequestDto;
import github.npcamp.teamtaskflow.domain.user.dto.response.UserResponseDto;
import github.npcamp.teamtaskflow.domain.auth.service.SignUpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class SignUpController {

    private final SignUpService signUpService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto){
        signUpService.signUp(signUpRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
