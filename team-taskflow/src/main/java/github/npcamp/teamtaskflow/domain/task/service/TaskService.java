package github.npcamp.teamtaskflow.domain.task.service;

import github.npcamp.teamtaskflow.domain.common.entity.Task;
import github.npcamp.teamtaskflow.domain.task.TaskStatus;
import github.npcamp.teamtaskflow.domain.task.dto.request.CreateTaskRequestDto;
import github.npcamp.teamtaskflow.domain.task.dto.request.UpdateTaskRequestDto;
import github.npcamp.teamtaskflow.domain.task.dto.response.CreateTaskResponseDto;
import github.npcamp.teamtaskflow.domain.task.dto.response.TaskDetailResponseDto;
import github.npcamp.teamtaskflow.domain.task.dto.response.TaskResponseDto;
import github.npcamp.teamtaskflow.global.payload.PageResponse;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    CreateTaskResponseDto createTask(CreateTaskRequestDto req, Long currentUserId);

    TaskDetailResponseDto getTask(Long taskId);

    PageResponse<TaskResponseDto> getTasks(Pageable pageable, TaskStatus status);

    TaskDetailResponseDto updateTask(Long taskId, UpdateTaskRequestDto req);

    TaskDetailResponseDto updateStatus(Long taskId, TaskStatus newStatus, Long currentUserId);

    void deleteTask(Long taskId);

    Task findTaskByIdOrElseThrow(Long taskId);

}
