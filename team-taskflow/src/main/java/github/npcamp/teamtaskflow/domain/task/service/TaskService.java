package github.npcamp.teamtaskflow.domain.task.service;

import github.npcamp.teamtaskflow.domain.task.TaskStatus;
import github.npcamp.teamtaskflow.domain.task.dto.request.CreateTaskRequestDto;
import github.npcamp.teamtaskflow.domain.task.dto.request.UpdateTaskRequestDto;
import github.npcamp.teamtaskflow.domain.task.dto.response.CreateTaskResponseDto;
import github.npcamp.teamtaskflow.domain.task.dto.response.TaskDetailResponseDto;
import github.npcamp.teamtaskflow.domain.task.dto.response.TaskResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    CreateTaskResponseDto createTask(CreateTaskRequestDto req);

    TaskDetailResponseDto getTask(Long taskId);

    Page<TaskResponseDto> getTasks(Pageable pageable);

    TaskDetailResponseDto updateTask(Long taskId, UpdateTaskRequestDto req);

    TaskDetailResponseDto updateStatus(Long taskId, TaskStatus newStatus);

    void deleteTask(Long taskId);
}
