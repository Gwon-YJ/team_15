package github.npcamp.teamtaskflow.domain.user.controller;


import github.npcamp.teamtaskflow.domain.user.dto.LoginResponseDto;
import github.npcamp.teamtaskflow.domain.user.dto.UserRequestDto;
import github.npcamp.teamtaskflow.domain.user.dto.UserResponseDto;
import github.npcamp.teamtaskflow.domain.user.service.UserService;
import github.npcamp.teamtaskflow.global.utils.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody UserRequestDto.Login userRequestDto) {
        String token  = userService.login(userRequestDto);
        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> signUp(@Valid @RequestBody UserRequestDto.SignUp userRequestDto){
        userService.signUp(userRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        userService.logOut(token.replace("Bearer ", ""));
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // 회원 전체조회
    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> findUserList(@Valid @RequestBody UserRequestDto.FindByName userRequestDto){
        List<UserResponseDto> resultDtoList = userService.findUserList(userRequestDto);
        return new ResponseEntity<>(resultDtoList, HttpStatus.OK);
    }

    // 회원 부분조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> findUser(@PathVariable Long userId){
        UserResponseDto resultDto = userService.findUser(userId);
        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }

    // 회원 수정
    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long userId,@RequestHeader("Authorization") String authorizationHeader, @Valid @RequestBody UserRequestDto.UpdateUser userRequestDto){
        UserResponseDto resultDto = userService.updateUser(userId, authorizationHeader, userRequestDto);
        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }

    // 회원 수정
    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUserPw(@PathVariable Long userId,@RequestHeader("Authorization") String authorizationHeader, @Valid @RequestBody UserRequestDto.UpdatePw userRequestDto){
        userService.updateUserPw(userId, authorizationHeader, userRequestDto);
        return new ResponseEntity<>("수정 성공", HttpStatus.OK);
    }

    // 회원 삭제
    @DeleteMapping("{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId,@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserRequestDto.DeleteUser userRequestDto){
        userService.deleteUser(userId, authorizationHeader, userRequestDto);
        return new ResponseEntity<>("삭제 성공", HttpStatus.OK);
    }

}
