package github.npcamp.teamtaskflow.domain.user.service;

import github.npcamp.teamtaskflow.domain.common.entity.User;
import github.npcamp.teamtaskflow.domain.user.dto.UserRequestDto;
import github.npcamp.teamtaskflow.domain.user.dto.UserResponseDto;
import github.npcamp.teamtaskflow.domain.user.enums.UserRoleEnum;
import github.npcamp.teamtaskflow.domain.user.repository.UserRepository;
import github.npcamp.teamtaskflow.global.exception.CustomException;
import github.npcamp.teamtaskflow.global.exception.ErrorCode;
import github.npcamp.teamtaskflow.global.utils.JwtUtil;
import github.npcamp.teamtaskflow.global.utils.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String login(UserRequestDto.Login userRequestDto) {
        String email = userRequestDto.getEmail();
        String password = userRequestDto.getPassword();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.PASSWORD_EMAIL_MISMATCH)
        );

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_EMAIL_MISMATCH);
        }

        return jwtUtil.generateToken(user.getUserId(), user.getRole());
    }

    public void signUp(UserRequestDto.SignUp userRequestDto) {
        if (userRepository.findByEmail(userRequestDto.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.RESOURCE_ALREADY_EXIST);
        }

        User user = new User(
                userRequestDto.getUserName(),
                passwordEncoder.encode(userRequestDto.getPassword()),
                userRequestDto.getEmail(),
                UserRoleEnum.USER
        );

        userRepository.save(user);
    }

    public void logOut(String token) {
        // 1. 토큰 유효성 검사
        if (!jwtUtil.validateToken(token)) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        }

        // 2. 블랙리스트 등록 (예: Redis 등)
//        long expiration = jwtUtil.getExpiration(token); // 남은 만료 시간
//        redisTemplate.opsForValue().set(token, "logout", expiration, TimeUnit.MILLISECONDS);
    }

    public List<UserResponseDto> findUserList(UserRequestDto.FindByName userRequestDto){
        List<User> userList = userRepository.findAllByUserNameOrderByModifiedAtDesc(userRequestDto.getUserName());
        List<UserResponseDto> userDtoList = userList.stream().map(user -> new UserResponseDto(user)).collect(Collectors.toList());
        //없으면 빈 리스트를 반환합니다.
        return userDtoList;
    }

    public UserResponseDto findUser(Long userId) {
        User user = isUserEmpty(userId);
        UserResponseDto userResponseDto = new UserResponseDto(user);

        return userResponseDto;
    }


    public UserResponseDto updateUser(Long userId, String authorizationHeader, UserRequestDto.UpdateUser userRequestDto) {
        String token = jwtUtil.extractBearerToken(authorizationHeader);  // "Bearer " 제거
        //토큰 만료 확인
        if(!jwtUtil.validateToken(token))
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);

        User user = isUserEmpty(userId);

        //접근 권한 확인
        String customId = jwtUtil.extractUsername(token);
        if(!customId.equals(user.getUserId()))
            throw new CustomException(ErrorCode.ACCESS_DENIED);

        // 비밀번호 검증
        if(!passwordEncoder.matches(userRequestDto.getPassword(), user.getPassword()))
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);

        //사용자 정보 변경
        if(userRequestDto.getUserName() != null)
            user.setUserName(userRequestDto.getUserName());
        if(userRequestDto.getEmail() != null)
            user.setEmail(userRequestDto.getEmail());
        userRepository.save(user);
        UserResponseDto userResponseDto = new UserResponseDto(user);

        return userResponseDto;
    }

    public void updateUserPw(Long userId, String authorizationHeader, UserRequestDto.UpdatePw userRequestDto) {
        String token = jwtUtil.extractBearerToken(authorizationHeader);  // "Bearer " 제거
        //토큰 만료 확인
        if(!jwtUtil.validateToken(token))
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);

        User user = isUserEmpty(userId);

        //접근 권한 확인
        String customId = jwtUtil.extractUsername(token);
        if(!customId.equals(user.getUserId()))
            throw new CustomException(ErrorCode.ACCESS_DENIED);

        // 비밀번호 검증
        if(!passwordEncoder.matches(userRequestDto.getSavePassword(), user.getPassword()))
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);

        // 비밀번호 변경
        user.setPassword(passwordEncoder.encode(userRequestDto.getChangePassword()));
        userRepository.save(user);
    }

    public void deleteUser(Long userId, String authorizationHeader, UserRequestDto.DeleteUser userRequestDto) {
        String token = jwtUtil.extractBearerToken(authorizationHeader);  // "Bearer " 제거
        //토큰 만료 확인
        if(!jwtUtil.validateToken(token))
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);

        User user = isUserEmpty(userId);

        //접근 권한 확인
        String customId = jwtUtil.extractUsername(token);
        if(!customId.equals(user.getUserId()))
            throw new CustomException(ErrorCode.ACCESS_DENIED);

        // 비밀번호 검증
        if(!passwordEncoder.matches(userRequestDto.getPassword(), user.getPassword()))
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);

        // 삭제
        userRepository.deleteById(userId);
    }

    private User isUserEmpty(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty())
            throw new CustomException(ErrorCode.ENTITY_NOT_FOUND);
        return optionalUser.get();
   }
}
