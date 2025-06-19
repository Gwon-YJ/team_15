package github.npcamp.teamtaskflow.domain.user.service;



import github.npcamp.teamtaskflow.domain.user.UserRoleEnum;
import github.npcamp.teamtaskflow.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

//    @Test
//    void 전체_조회_성공() {
//        // given
//        List<User> userList = List.of(
//                new User(1L,"user1", "user1@example.com", "password", "이순신", UserRoleEnum.USER),
//                new User(1L,"user2", "user2@example.com", "password", "홍길동", UserRoleEnum.USER)
//        );
//
//        when(userRepository.findAll()).thenReturn(userList);
//
//        // when
//        List<User> result = userService.getAllUsers();
//
//        // then
//        assertThat(result).hasSize(2);
//        verify(userRepository, times(1)).findAll();
//    }
//
//    @Test
//    void 단일_조회_성공() {
//        // given
//        User user = new User(1L,"user1", "user1@example.com", "password", "이순신", UserRoleEnum.USER);
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        // when
//        User result = userService.getUserById(1L);
//
//        // then
//        assertThat(result.getUsername()).isEqualTo("user1");
//    }
//
//    @Test
//    void deleteUser_삭제_성공() {
//        // given
//        User user = new User( 1L,"user1", "user1@example.com", "password", "이순신", UserRoleEnum.USER);
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        // when
//        userService.deleteUser(1L);
//
//        // then
//        verify(userRepository, times(1)).delete(user);
//    }
}
