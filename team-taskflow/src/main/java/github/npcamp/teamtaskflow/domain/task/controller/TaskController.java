package github.npcamp.teamtaskflow.domain.task.controller;

import github.npcamp.teamtaskflow.domain.task.dto.CreateTaskRequestDto;
import github.npcamp.teamtaskflow.domain.task.dto.CreateTaskResponseDto;
import github.npcamp.teamtaskflow.domain.task.dto.TaskResponseDto;
import github.npcamp.teamtaskflow.domain.task.dto.TaskDetailResponseDto;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateTaskResponseDto>> createTask(@Valid @RequestBody CreateTaskRequestDto req) {
        CreateTaskResponseDto res = taskService.createTask(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(res));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskDetailResponseDto>> getTask(@PathVariable Long taskId) {
        TaskDetailResponseDto res = taskService.getTask(taskId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(res));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TaskResponseDto>>> getTasks(@PageableDefault(sort = "dueDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TaskResponseDto> tasks = taskService.getTasks(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(tasks));
    }
}
