package github.npcamp.teamtaskflow.domain.task.service;

import github.npcamp.teamtaskflow.domain.task.dto.CreateTaskRequestDto;
import github.npcamp.teamtaskflow.domain.task.dto.CreateTaskResponseDto;
import github.npcamp.teamtaskflow.domain.task.dto.TaskResponseDto;
import github.npcamp.teamtaskflow.domain.task.dto.TaskDetailResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    CreateTaskResponseDto createTask(CreateTaskRequestDto req);

    TaskDetailResponseDto getTask(Long taskId);

    Page<TaskResponseDto> getTasks(Pageable pageable);
}
