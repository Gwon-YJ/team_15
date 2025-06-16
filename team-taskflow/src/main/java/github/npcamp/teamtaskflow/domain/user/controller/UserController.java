package github.npcamp.teamtaskflow.domain.user.controller;


import github.npcamp.teamtaskflow.domain.user.dto.request.UserRequestDto;
import github.npcamp.teamtaskflow.domain.user.dto.response.UserResponseDto;
import github.npcamp.teamtaskflow.domain.user.service.UserService;
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


    // 회원 전체조회
    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> findUserList(@Valid @RequestBody UserRequestDto userRequestDto){
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
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long userId,@RequestHeader("Authorization") String authorizationHeader, @Valid @RequestBody UserRequestDto userRequestDto){
        UserResponseDto resultDto = userService.updateUser(userId, authorizationHeader, userRequestDto);
        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }

    // 회원 비밀번호수정
    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUserPw(@PathVariable Long userId,@RequestHeader("Authorization") String authorizationHeader, @Valid @RequestBody UserRequestDto userRequestDto){
        userService.updateUserPw(userId, authorizationHeader, userRequestDto);
        return new ResponseEntity<>("수정 성공", HttpStatus.OK);
    }

    // 회원 삭제
    @DeleteMapping("{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId,@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserRequestDto userRequestDto){
        userService.deleteUser(userId, authorizationHeader, userRequestDto);
        return new ResponseEntity<>("삭제 성공", HttpStatus.OK);
    }

}
