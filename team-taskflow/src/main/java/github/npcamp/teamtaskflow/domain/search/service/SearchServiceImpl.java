package github.npcamp.teamtaskflow.domain.search.service;

import github.npcamp.teamtaskflow.domain.task.dto.TaskResponseDto;
import github.npcamp.teamtaskflow.domain.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final TaskRepository taskRepository;

    /**
     * SELECT * FROM task
     * WHERE LOWER(title) LIKE LOWER('%keyword%')
     *    OR LOWER(content) LIKE LOWER('%keyword%')
     *    자동 쿼리생성 예시
     */
    @Override
    public Page<TaskResponseDto> searchTasks(String keyword, Pageable pageable) {
        return taskRepository
                .findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable)
                .map(TaskResponseDto::toDto);
    }
}