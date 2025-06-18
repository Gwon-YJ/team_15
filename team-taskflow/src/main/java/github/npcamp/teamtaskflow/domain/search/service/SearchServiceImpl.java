package github.npcamp.teamtaskflow.domain.search.service;

import github.npcamp.teamtaskflow.domain.comment.dto.response.CommentResponseListDto;
import github.npcamp.teamtaskflow.domain.comment.repository.CommentRepository;
import github.npcamp.teamtaskflow.domain.common.entity.Comment;
import github.npcamp.teamtaskflow.domain.task.dto.response.TaskResponseDto;
import github.npcamp.teamtaskflow.domain.task.repository.TaskRepository;
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

    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponseListDto> searchComments(Long taskId, String keyword, Pageable pageable) {
        Page<Comment> comments = commentRepository
                .findByTaskIdAndContentContainingIgnoreCase(taskId, keyword, pageable);
        return comments.map(CommentResponseListDto::toDto);
    }
}