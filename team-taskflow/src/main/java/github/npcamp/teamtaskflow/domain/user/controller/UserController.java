package github.npcamp.teamtaskflow.domain.user.controller;

import github.npcamp.teamtaskflow.domain.user.dto.response.UserResponseDto;
import github.npcamp.teamtaskflow.domain.user.service.UserService;
import github.npcamp.teamtaskflow.global.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

//    @GetMapping
//    public ResponseEntity<ApiResponse<Page<UserResponseDto>>> getUsers(@PageableDefault(sort = "name", direction = Sort.Direction.DESC) Pageable pageable) {
//        Page<UserResponseDto> users = userService.getUsers(pageable);
//        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(users));
//    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> getCurrentUser(@AuthenticationPrincipal Long currentUserId) {
        UserResponseDto user = userService.getCurrentUser(currentUserId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("사용자 정보를 조회했습니다.", user));
    }
}
