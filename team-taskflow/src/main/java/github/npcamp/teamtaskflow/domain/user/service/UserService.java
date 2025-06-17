package github.npcamp.teamtaskflow.domain.user.service;

import github.npcamp.teamtaskflow.domain.common.entity.User;
import github.npcamp.teamtaskflow.domain.user.dto.request.PasswordChangeRequestDto;
import github.npcamp.teamtaskflow.domain.user.dto.request.UserRequestDto;
import github.npcamp.teamtaskflow.domain.user.dto.response.UserResponseDto;
import github.npcamp.teamtaskflow.domain.user.repository.UserRepository;
import github.npcamp.teamtaskflow.global.exception.CustomException;
import github.npcamp.teamtaskflow.global.exception.ErrorCode;
import github.npcamp.teamtaskflow.global.utils.JwtUtil;
import github.npcamp.teamtaskflow.global.utils.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    // 계정 전체 조회
    public List<UserResponseDto> findUserList(String userName) {
        List<User> userList;

        if (userName == null || userName.isBlank()) {
            // userName이 없으면 전체 사용자 조회, 업데이트 일자 내림차순 정렬
            userList = userRepository.findAllByOrderByUpdatedAtDesc();
        } else {
            // userName이 있으면 부분 일치 검색 (예: contains)
            userList = userRepository.findByUserNameContainingOrderByUpdatedAtDesc(userName);
        }

        return userList.stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }

    // 계정 부분 수정
    public UserResponseDto findUser(Long userId) {
        return new UserResponseDto(findUserOrThrow(userId));
    }

    // 계정 전체 수정
    public UserResponseDto updateUser(Long userId, String authorizationHeader, UserRequestDto userRequestDto) {
        User user = authenticateAndGetUser(userId, authorizationHeader);

        if (!passwordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        if (userRequestDto.getUserName() != null)
            user.setUserName(userRequestDto.getUserName());
        if (userRequestDto.getEmail() != null)
            user.setEmail(userRequestDto.getEmail());

        userRepository.save(user);
        return new UserResponseDto(user);
    }

    // 계정 비밀번호 수정
    public void updateUserPw(Long userId, String authorizationHeader, PasswordChangeRequestDto dto) {
        User user = authenticateAndGetUser(userId, authorizationHeader);

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    // 계정 삭제
    public void deleteUser(Long userId, String authorizationHeader, UserRequestDto userRequestDto) {
        User user = authenticateAndGetUser(userId, authorizationHeader);

        if (!passwordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        userRepository.deleteById(userId);
    }


    private User authenticateAndGetUser(Long userId, String authorizationHeader) {
        String token = jwtUtil.extractBearerToken(authorizationHeader);

        if (!jwtUtil.validateToken(token)) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        }

        User user = findUserOrThrow(userId);

        String customId = jwtUtil.extractUsername(token);
        if (!String.valueOf(user.getId()).equals(customId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        return user;
    }

    private User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));
    }
}

