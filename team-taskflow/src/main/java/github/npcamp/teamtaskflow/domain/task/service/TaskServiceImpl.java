package github.npcamp.teamtaskflow.domain.task.service;

import github.npcamp.teamtaskflow.domain.common.entity.Task;
import github.npcamp.teamtaskflow.domain.task.dto.CreateTaskRequestDto;
import github.npcamp.teamtaskflow.domain.task.dto.CreateTaskResponseDto;
import github.npcamp.teamtaskflow.domain.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
//    private final UserService userService;
//    private final ActivityLogService activityLogService; TODO

    @Override
    public CreateTaskResponseDto createTask(CreateTaskRequestDto req) {

//        User assignee = userService.findById(req.getAssigneeId()); TODO

        Task task = Task.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .priority(req.getPriority())
//                .assignee(assignee) TODO
                .dueDate(req.getDueDate())
                .build();

        Task savedTask = taskRepository.save(task);

        return CreateTaskResponseDto.toDto(savedTask);
    }
}
