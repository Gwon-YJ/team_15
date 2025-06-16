package github.npcamp.teamtaskflow.domain.search.service;

import github.npcamp.teamtaskflow.domain.task.dto.TaskResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchService {
    Page<TaskResponseDto> searchTasks(String keyword, Pageable pageable);
}
