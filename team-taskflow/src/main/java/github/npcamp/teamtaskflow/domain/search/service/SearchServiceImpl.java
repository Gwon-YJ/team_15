package github.npcamp.teamtaskflow.domain.search.service;

import github.npcamp.teamtaskflow.domain.comment.dto.response.CommentDetailDto;
import github.npcamp.teamtaskflow.domain.comment.repository.CommentRepository;
import github.npcamp.teamtaskflow.domain.task.dto.response.TaskResponseDto;
import github.npcamp.teamtaskflow.domain.task.repository.TaskRepository;
import github.npcamp.teamtaskflow.global.payload.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;

    /**
     * SELECT * FROM task
     * WHERE LOWER(title) LIKE LOWER('%keyword%')
     *    OR LOWER(description) LIKE LOWER('%keyword%')
     *    자동 쿼리생성 예시
     */
    @Override
    public Page<TaskResponseDto> searchTasks(String keyword, Pageable pageable) {
        return taskRepository
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword, pageable)
                .map(TaskResponseDto::toDto);
    }

    /**
     *
     * SELECT * FROM comment
     * WHERE LOWER(content) LIKE LOWER('%keyword%')
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<CommentDetailDto> searchComments(String keyword, Pageable pageable) {
        Page<CommentDetailDto> comments = commentRepository
                .findByContentContainingIgnoreCase(keyword, pageable)
                .map(CommentDetailDto::toDto);
        return new PageResponse<>(comments);
    }
}