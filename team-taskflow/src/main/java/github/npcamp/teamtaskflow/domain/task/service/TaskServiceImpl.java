package github.npcamp.teamtaskflow.domain.task.service;

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

    // TODO: 로그인한 사용자 추가
    @Override
    @Transactional
    public CreateTaskResponseDto createTask(CreateTaskRequestDto req) {

        // 유저 조회
        User assignee = userRepository.findById(req.getAssigneeId())
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        // Task 생성 (기본 Status는 "할 일")
        Task task = Task.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .priority(req.getPriority())
                .assignee(assignee)
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
    public Page<TaskResponseDto> getTasks(Pageable pageable) {

        // Task 페이지 조회
        Page<Task> tasks = taskRepository.findAll(pageable);

        return tasks.map(TaskResponseDto::toDto);
    }

    /**
     * Task 제목, 내용, 우선순위, 담당자, 마감일 수정
     */

    // TODO: 접근 권한 설정
    @Override
    @Transactional
    public TaskDetailResponseDto updateTask(Long taskId, UpdateTaskRequestDto req) {

        // Task 조회
        Task task = findTaskByIdOrElseThrow(taskId);

        // User 조회
        User assignee = userRepository.findById(req.getAssigneeId())
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND)); // 추후 수정 예정

        // Task 수정 (PUT)
        task.updateTask(req.getTitle(), req.getContent(), req.getPriority(), assignee, req.getDueDate());

        return TaskDetailResponseDto.toDto(taskRepository.save(task));
    }

    /**
     * Task 상태 수정
     * TODO -> IN_PROGRESS -> DONE 순으로 변경 가능
     */

    // TODO: 접근 권한 설정
    @Override
    @Transactional
    public TaskDetailResponseDto updateStatus(Long taskId, TaskStatus newStatus) {

        // task 조회
        Task task = findTaskByIdOrElseThrow(taskId);

        // TODO: 관리자만 변경 가능하도록 설정

        // 현재 상태에서 newStatus로 전환 가능 여부 검증
        TaskStatus oldStatus = task.getStatus();
        if (!oldStatus.canTransitionTo(newStatus)) {
            throw new TaskException(ErrorCode.INVALID_STATUS_TRANSITION);
        }

        // Task Status 수정(PATCH)
        task.updateStatus(newStatus);

        return TaskDetailResponseDto.toDto(taskRepository.save(task));
    }

    /**
     * Task 삭제
     * soft delete
     */
    // TODO: 접근 권한 설정
    @Override
    @Transactional
    public void deleteTask(Long taskId) {
        Task task = findTaskByIdOrElseThrow(taskId);
        taskRepository.delete(task);
    }

    public Task findTaskByIdOrElseThrow(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskException(ErrorCode.TASK_NOT_FOUND));
    }

}
