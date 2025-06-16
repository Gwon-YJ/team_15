package github.npcamp.teamtaskflow.domain.auth.Controller;

import github.npcamp.teamtaskflow.domain.auth.service.LogOutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class LogOutController {

    private final LogOutService logOutService;

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        logOutService.logOut(token.replace("Bearer ", ""));
        return ResponseEntity.noContent().build(); // 204 No Content
    }

}
