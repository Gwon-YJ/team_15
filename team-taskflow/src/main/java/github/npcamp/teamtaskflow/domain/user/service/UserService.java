package github.npcamp.teamtaskflow.domain.user.service;

import github.npcamp.teamtaskflow.domain.common.entity.User;
import github.npcamp.teamtaskflow.domain.user.dto.response.UserResponseDto;
import github.npcamp.teamtaskflow.domain.user.exception.UserException;
import github.npcamp.teamtaskflow.domain.user.repository.UserRepository;
import github.npcamp.teamtaskflow.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDto getCurrentUser(Long currentUserId) {
        User user = findUserByIdOrElseThrow(currentUserId);
        return UserResponseDto.toDto(user);
    }

//    public Page<UserResponseDto> getUsers(Pageable pageable) {
//        Page<User> users = userRepository.findAll(pageable);
//        return users.map(UserResponseDto::toDto);
//    }



    public User findUserByIdOrElseThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    public User findUserByUsernameOrElseThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }
}
