package github.npcamp.teamtaskflow.domain.comment.service;

import github.npcamp.teamtaskflow.domain.comment.dto.CreateCommentRequestDto;
import github.npcamp.teamtaskflow.domain.comment.dto.CreateCommentResponseDto;
import github.npcamp.teamtaskflow.domain.comment.exception.CommentNotFoundException;
import github.npcamp.teamtaskflow.domain.comment.repository.CommentRepository;
import github.npcamp.teamtaskflow.domain.common.entity.Comment;
import github.npcamp.teamtaskflow.domain.common.entity.Task;
import github.npcamp.teamtaskflow.domain.task.exception.TaskNotFoundException;
import github.npcamp.teamtaskflow.domain.task.repository.TaskRepository;
import github.npcamp.teamtaskflow.domain.task.service.TaskService;
import github.npcamp.teamtaskflow.global.exception.ErrorCode;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
//    private final TaskService taskService;
//    private final UserService userService;

    /**
     * 댓글 생성
     */
    @Override
    @Transactional
    public CreateCommentResponseDto createComment(Long taskId, Long userId, CreateCommentRequestDto requestDto) {

//        Task task = taskService.findByIdOrElseThrow(taskId);
//        User assignee = userService.findByIdOrElseThrow(userId);

        Comment comment = Comment.builder()
//                .task(task)
//                .assignee(assignee)
                .content(requestDto.content())
                .build();

        Comment saved = commentRepository.save(comment);

        return CreateCommentResponseDto.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Comment findByIdOrElseThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(ErrorCode.COMMENT_NOT_FOUND));
    }
}
