package github.npcamp.teamtaskflow.domain.task.service;

import github.npcamp.teamtaskflow.domain.common.entity.Task;
import github.npcamp.teamtaskflow.domain.common.entity.User;
import github.npcamp.teamtaskflow.domain.task.dto.CreateTaskRequestDto;
import github.npcamp.teamtaskflow.domain.task.dto.CreateTaskResponseDto;
import github.npcamp.teamtaskflow.domain.task.dto.TaskResponseDto;
import github.npcamp.teamtaskflow.domain.task.dto.TaskDetailResponseDto;
import github.npcamp.teamtaskflow.domain.task.exception.TaskNotFoundException;
import github.npcamp.teamtaskflow.domain.task.repository.TaskRepository;
import github.npcamp.teamtaskflow.domain.user.UserRepository;
import github.npcamp.teamtaskflow.domain.user.exception.UserNotFoundException;
import github.npcamp.teamtaskflow.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    /**
     * Task 생성
     */
    @Override
    @Transactional
    public CreateTaskResponseDto createTask(CreateTaskRequestDto req) {

        User assignee = userRepository.findById(req.getAssigneeId())
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        Task task = Task.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .priority(req.getPriority())
                .assignee(assignee)
                .dueDate(req.getDueDate())
                .build();

        Task savedTask = taskRepository.save(task);

        return CreateTaskResponseDto.toDto(savedTask);
    }

    /**
     * Task 단건과 댓글 목록을 조회
     */
    @Override
    @Transactional(readOnly = true)
    public TaskDetailResponseDto getTask(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(ErrorCode.TASK_NOT_FOUND));

        return TaskDetailResponseDto.toDto(task);
    }

    /**
     * Task page조회 defalut size = 10
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponseDto> getTasks(Pageable pageable) {
        Page<Task> tasks = taskRepository.findAll(pageable);
        return tasks.map(TaskResponseDto::toDto);
    }
}
