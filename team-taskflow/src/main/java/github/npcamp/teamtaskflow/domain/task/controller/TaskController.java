package github.npcamp.teamtaskflow.domain.task.controller;

import github.npcamp.teamtaskflow.domain.task.dto.CreateTaskRequestDto;
import github.npcamp.teamtaskflow.domain.task.dto.CreateTaskResponseDto;
import github.npcamp.teamtaskflow.domain.task.service.TaskService;
import github.npcamp.teamtaskflow.global.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateTaskResponseDto>> createTask(@Valid @RequestBody CreateTaskRequestDto req) {
        CreateTaskResponseDto res = taskService.createTask(req);
        return ResponseEntity.ok(ApiResponse.success(res));
    }
}
