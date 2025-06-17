package github.npcamp.teamtaskflow.domain.user.controller;

import github.npcamp.teamtaskflow.domain.user.dto.request.PasswordChangeRequestDto;
import github.npcamp.teamtaskflow.domain.user.dto.request.UserRequestDto;
import github.npcamp.teamtaskflow.domain.user.dto.response.UserResponseDto;
import github.npcamp.teamtaskflow.domain.user.service.UserService;
import github.npcamp.teamtaskflow.global.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    // 계정 전체조회 (userName으로 검색)
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> findUserList(
            @RequestParam(required = false) String userName) {
        List<UserResponseDto> users = userService.findUserList(userName);
        return ResponseEntity.ok(ApiResponse.success("사용자 리스트 조회 완료", users));
    }

    // 계정 단일조회
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> findUser(@PathVariable Long userId) {
       UserResponseDto user = userService.findUser(userId);
        return ResponseEntity.ok(ApiResponse.success("사용자 정보를 조회했습니다.", user));
    }

    // 계정 정보 수정
    @PatchMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(
            @PathVariable Long userId,
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody UserRequestDto userRequestDto
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("회원 정보가 수정되었습니다.", userService.updateUser(userId, authorizationHeader, userRequestDto))
        );
    }

    // 계정 비밀번호 수정
    @PatchMapping("/{userId}/password")
    public ResponseEntity<ApiResponse<Void>> updateUserPw(
            @PathVariable Long userId,
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody PasswordChangeRequestDto dto
    ) {
        userService.updateUserPw(userId, authorizationHeader, dto);
        return ResponseEntity.ok(ApiResponse.success("비밀번호가 성공적으로 변경되었습니다."));
    }

    // 계정 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable Long userId,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UserRequestDto userRequestDto
    ) {
        userService.deleteUser(userId, authorizationHeader, userRequestDto);
        return ResponseEntity.ok(ApiResponse.success("회원이 성공적으로 삭제되었습니다."));
    }
}

