package github.npcamp.teamtaskflow.domain.task.service;

import github.npcamp.teamtaskflow.domain.task.dto.CreateTaskRequestDto;
import github.npcamp.teamtaskflow.domain.task.dto.CreateTaskResponseDto;

public interface TaskService {
    CreateTaskResponseDto createTask(CreateTaskRequestDto req);
}
