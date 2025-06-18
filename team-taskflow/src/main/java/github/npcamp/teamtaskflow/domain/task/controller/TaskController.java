package github.npcamp.teamtaskflow.domain.task.controller;

import github.npcamp.teamtaskflow.domain.task.TaskStatus;
import github.npcamp.teamtaskflow.domain.user.exception.UserException;
import github.npcamp.teamtaskflow.domain.user.repository.UserRepository;
import github.npcamp.teamtaskflow.global.exception.ErrorCode;
import github.npcamp.teamtaskflow.domain.common.entity.User;

import github.npcamp.teamtaskflow.domain.task.dto.request.CreateTaskRequestDto;
import github.npcamp.teamtaskflow.domain.task.dto.request.UpdateStatusRequestDto;
import github.npcamp.teamtaskflow.domain.task.dto.request.UpdateTaskRequestDto;
import github.npcamp.teamtaskflow.domain.task.dto.response.CreateTaskResponseDto;
import github.npcamp.teamtaskflow.domain.task.dto.response.TaskDetailResponseDto;
import github.npcamp.teamtaskflow.domain.task.dto.response.TaskResponseDto;
import github.npcamp.teamtaskflow.domain.task.service.TaskService;
import github.npcamp.teamtaskflow.global.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateTaskResponseDto>> createTask(@Valid @RequestBody CreateTaskRequestDto req,
                                                                         @AuthenticationPrincipal Long currentUserId) {

        System.out.println(currentUserId.toString());

        CreateTaskResponseDto res = taskService.createTask(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(res));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskDetailResponseDto>> getTask(@PathVariable Long taskId) {
        TaskDetailResponseDto res = taskService.getTask(taskId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(res));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TaskResponseDto>>> getTasks(@PageableDefault(sort = "dueDate", direction = Sort.Direction.DESC) Pageable pageable,
                                                                       @RequestParam(required = false) TaskStatus status) {
        Page<TaskResponseDto> tasks = taskService.getTasks(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(tasks));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskDetailResponseDto>> updateTask(@PathVariable Long taskId,
                                                                         @Valid @RequestBody UpdateTaskRequestDto req) {
        TaskDetailResponseDto res = taskService.updateTask(taskId, req);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(res));
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<ApiResponse<TaskDetailResponseDto>> updateStatus(@PathVariable Long taskId,
                                                                           @Valid @RequestBody UpdateStatusRequestDto req) {
        TaskDetailResponseDto res = taskService.updateStatus(taskId, req.getNewStatus());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(res));
    }

    @DeleteMapping("/{taskId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Task가 삭제되었습니다."));
    }
}
