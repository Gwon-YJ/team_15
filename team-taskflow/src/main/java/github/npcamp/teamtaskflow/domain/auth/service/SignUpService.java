package github.npcamp.teamtaskflow.domain.auth.service;

import github.npcamp.teamtaskflow.domain.common.entity.User;
import github.npcamp.teamtaskflow.domain.auth.dto.request.SignUpRequestDto;
import github.npcamp.teamtaskflow.domain.user.enums.UserRoleEnum;
import github.npcamp.teamtaskflow.domain.auth.repository.SignUpRepository;
import github.npcamp.teamtaskflow.global.exception.CustomException;
import github.npcamp.teamtaskflow.global.exception.ErrorCode;
import github.npcamp.teamtaskflow.global.utils.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SignUpService {

    private final SignUpRepository signUpRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpRequestDto signUpRequestDto) {
        if (signUpRepository.findByUserName(signUpRequestDto.getUserName()).isPresent()) {
            throw new CustomException(ErrorCode.RESOURCE_ALREADY_EXIST);
        }

        User user = new User(
                signUpRequestDto.getUserName(),
                passwordEncoder.encode(signUpRequestDto.getPassword()),
                signUpRequestDto.getEmail(),
                UserRoleEnum.USER
        );

        signUpRepository.save(user);
    }
}

