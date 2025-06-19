package github.npcamp.teamtaskflow.domain.task.controller;

import github.npcamp.teamtaskflow.domain.common.aop.Logging;
import github.npcamp.teamtaskflow.domain.log.ActivityType;
import github.npcamp.teamtaskflow.domain.task.TaskStatus;
import github.npcamp.teamtaskflow.domain.task.dto.request.CreateTaskRequestDto;
import github.npcamp.teamtaskflow.domain.task.dto.request.UpdateStatusRequestDto;
import github.npcamp.teamtaskflow.domain.task.dto.request.UpdateTaskRequestDto;
import github.npcamp.teamtaskflow.domain.task.dto.response.CreateTaskResponseDto;
import github.npcamp.teamtaskflow.domain.task.dto.response.TaskDetailResponseDto;
import github.npcamp.teamtaskflow.domain.task.dto.response.TaskResponseDto;
import github.npcamp.teamtaskflow.domain.task.service.TaskService;
import github.npcamp.teamtaskflow.global.payload.ApiResponse;
import github.npcamp.teamtaskflow.global.payload.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    @Logging(ActivityType.TASK_CREATED)
    public ResponseEntity<ApiResponse<CreateTaskResponseDto>> createTask(@Valid @RequestBody CreateTaskRequestDto req,
                                                                         @AuthenticationPrincipal Long currentUserId) {
        CreateTaskResponseDto res = taskService.createTask(req, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(res));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskDetailResponseDto>> getTask(@PathVariable Long taskId) {
        TaskDetailResponseDto res = taskService.getTask(taskId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(res));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TaskResponseDto>>> getTasks(@PageableDefault(sort = "dueDate", direction = Sort.Direction.DESC) Pageable pageable,
                                                                               @RequestParam TaskStatus status) {
        PageResponse<TaskResponseDto> tasks = taskService.getTasks(pageable, status);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(tasks));
    }

    @PutMapping("/{taskId}")
    @Logging(ActivityType.TASK_UPDATED)
    public ResponseEntity<ApiResponse<TaskDetailResponseDto>> updateTask(@PathVariable Long taskId,
                                                                         @Valid @RequestBody UpdateTaskRequestDto req) {
        TaskDetailResponseDto res = taskService.updateTask(taskId, req);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(res));
    }

    @PatchMapping("/{taskId}/status")
    @Logging(ActivityType.TASK_STATUS_CHANGED)
    public ResponseEntity<ApiResponse<TaskDetailResponseDto>> updateStatus(@AuthenticationPrincipal Long currentUserId,
                                                                           @PathVariable Long taskId,
                                                                           @Valid @RequestBody UpdateStatusRequestDto req) {
        TaskDetailResponseDto res = taskService.updateStatus(taskId, req.getStatus(), currentUserId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(res));
    }

    @DeleteMapping("/{taskId}")
    @Logging(ActivityType.TASK_DELETED)
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Task가 삭제되었습니다."));
    }
}
