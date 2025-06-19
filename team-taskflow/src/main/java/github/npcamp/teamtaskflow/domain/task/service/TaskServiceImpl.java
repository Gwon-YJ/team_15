package github.npcamp.teamtaskflow.domain.task.service;

import github.npcamp.teamtaskflow.domain.auth.exception.AuthException;
import github.npcamp.teamtaskflow.domain.common.entity.Task;
import github.npcamp.teamtaskflow.domain.common.entity.User;
import github.npcamp.teamtaskflow.domain.task.TaskStatus;
import github.npcamp.teamtaskflow.domain.task.dto.request.CreateTaskRequestDto;
import github.npcamp.teamtaskflow.domain.task.dto.request.UpdateTaskRequestDto;
import github.npcamp.teamtaskflow.domain.task.dto.response.CreateTaskResponseDto;
import github.npcamp.teamtaskflow.domain.task.dto.response.TaskDetailResponseDto;
import github.npcamp.teamtaskflow.domain.task.dto.response.TaskResponseDto;
import github.npcamp.teamtaskflow.domain.task.exception.TaskException;
import github.npcamp.teamtaskflow.domain.task.repository.TaskRepository;
import github.npcamp.teamtaskflow.domain.user.UserRoleEnum;
import github.npcamp.teamtaskflow.domain.user.service.UserService;
import github.npcamp.teamtaskflow.global.exception.ErrorCode;
import github.npcamp.teamtaskflow.global.payload.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    /**
     * Task 생성
     */
    @Override
    @Transactional
    public CreateTaskResponseDto createTask(CreateTaskRequestDto req, Long currentUserId) {

        // Task 담당자 조회
        User assignee = userService.findUserByIdOrElseThrow(req.getAssigneeId());

        // 로그인한 사용자 조회
        User creator = userService.findUserByIdOrElseThrow(currentUserId);

        // Task 생성 (기본 Status는 "할 일")
        Task task = Task.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .priority(req.getPriority())
                .assignee(assignee)
                .creator(creator)
                .dueDate(req.getDueDate())
                .build();

        // 영속화
        Task savedTask = taskRepository.save(task);

        return CreateTaskResponseDto.toDto(savedTask);
    }

    /**
     * Task 단건과 댓글 목록을 조회
     */
    @Override
    @Transactional(readOnly = true)
    public TaskDetailResponseDto getTask(Long taskId) {
        Task task = findTaskByIdOrElseThrow(taskId);
        return TaskDetailResponseDto.toDto(task);
    }

    /**
     * Task page조회 defalut size = 10
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<TaskResponseDto> getTasks(Pageable pageable, TaskStatus status) {
        Page<Task> tasks = taskRepository.findTasksByStatus(pageable, status);
        return new PageResponse<>(tasks.map(TaskResponseDto::toDto));
    }

    /**
     * Task 제목, 내용, 우선순위, 담당자, 마감일 수정
     */
    @Override
    @Transactional
    public TaskDetailResponseDto updateTask(Long taskId, UpdateTaskRequestDto req) {

        // Task 조회
        Task task = findTaskByIdOrElseThrow(taskId);

        // User 조회
        User assignee = userService.findUserByIdOrElseThrow(req.getAssigneeId());

        // Task 수정 (PUT)
        task.updateTask(req.getTitle(), req.getDescription(), req.getPriority(), assignee, req.getDueDate());

        return TaskDetailResponseDto.toDto(taskRepository.saveAndFlush(task));
    }

    /**
     * Task 상태 수정
     * 할일 -> 진행 중 -> 완료 순으로 변경 가능
     */
    @Override
    @Transactional
    public TaskDetailResponseDto updateStatus(Long taskId, TaskStatus newStatus, Long currentUserId) {

        // task 조회
        Task task = findTaskByIdOrElseThrow(taskId);

        // 현재 로그인된 사용자
        User currentUser = userService.findUserByIdOrElseThrow(currentUserId);

        boolean isAssignee = task.getAssignee().getId().equals(currentUserId);
        boolean isAdmin    = currentUser.getRole() == UserRoleEnum.ADMIN;

        // 로그인된 사용자가 ADMIN이 아니거나, 담당자가 아니면 ACCESS_DENIED
        if (!isAssignee && !isAdmin) {
            throw new AuthException(ErrorCode.ACCESS_DENIED);
        }

        // 현재 상태에서 newStatus로 전환 가능 여부 검증
        TaskStatus oldStatus = task.getStatus();
        if (!oldStatus.canTransitionTo(newStatus)) {
            throw new TaskException(ErrorCode.INVALID_STATUS_TRANSITION);
        }

        // Task Status 수정(PUT)
        task.updateStatus(newStatus);

        return TaskDetailResponseDto.toDto(taskRepository.saveAndFlush(task));
    }

    /**
     * Task 삭제
     * soft delete
     */
    @Override
    @Transactional
    @Secured("ROLE_ADMIN")
    public void deleteTask(Long taskId) {
        Task task = findTaskByIdOrElseThrow(taskId);
        taskRepository.delete(task);
    }

    @Override
    @Transactional
    public Task findTaskByIdOrElseThrow(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskException(ErrorCode.TASK_NOT_FOUND));
    }

}
