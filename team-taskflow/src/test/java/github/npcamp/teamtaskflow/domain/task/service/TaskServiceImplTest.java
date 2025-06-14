package github.npcamp.teamtaskflow.domain.task.service;

import github.npcamp.teamtaskflow.domain.common.entity.Task;
import github.npcamp.teamtaskflow.domain.common.entity.User;
import github.npcamp.teamtaskflow.domain.task.TaskPriority;
import github.npcamp.teamtaskflow.domain.task.dto.CreateTaskRequestDto;
import github.npcamp.teamtaskflow.domain.task.dto.CreateTaskResponseDto;
import github.npcamp.teamtaskflow.domain.task.dto.TaskDetailResponseDto;
import github.npcamp.teamtaskflow.domain.task.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @InjectMocks
    TaskServiceImpl taskService;

    @Mock
    TaskRepository taskRepository;

    @Test
    void 태스크_저장_서비스_단위_테스트() {

        // given
        CreateTaskRequestDto dto = new CreateTaskRequestDto("테스트 제목", "테스트 내용", TaskPriority.LOW, 1L, LocalDateTime.now().plusDays(1));

        User user = mock();
        ReflectionTestUtils.setField(user, "id", dto.getAssigneeId());

        Task savedTask = Task.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .priority(dto.getPriority())
                .assignee(user)
                .dueDate(dto.getDueDate())
                .build();

        given(taskRepository.save(any(Task.class))).willReturn(savedTask);

        // when
        CreateTaskResponseDto responseDto = taskService.createTask(dto);

        // then

        // 상태검증
        assertEquals(dto.getTitle(), responseDto.getTitle());
        assertEquals(dto.getContent(), responseDto.getContent());
        assertEquals(dto.getPriority(), responseDto.getPriority());
        assertEquals(user.getUsername(), responseDto.getAssignee());
        assertEquals(dto.getDueDate(), responseDto.getDueDate());
    }

    @Test
    void 태스크단건_정상조회() {
        // given
        long taskId = 1L;

        User user = mock();
        ReflectionTestUtils.setField(user, "username", "테스트 이름");

        Task task = Task.builder()
                .assignee(user)
                .build();

        given(taskRepository.findById(anyLong())).willReturn(Optional.of(task));

        // when
        TaskDetailResponseDto res = taskService.getTask(taskId);

        // then
        assertNotNull(res);
    }

    @Test
    void 태스크단건_조회실패() {
        // given
        long taskId = 1L;
        given(taskRepository.findById(anyLong())).willReturn(null);

        // when & then
        assertThrows(NullPointerException.class, () -> taskService.getTask(taskId));
    }
}