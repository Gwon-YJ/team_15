package github.npcamp.teamtaskflow.domain.comment.service;

import github.npcamp.teamtaskflow.domain.comment.dto.request.CreateCommentRequestDto;
import github.npcamp.teamtaskflow.domain.comment.dto.request.UpdateCommentRequestDto;
import github.npcamp.teamtaskflow.domain.comment.dto.response.CommentDeleteResponseDto;
import github.npcamp.teamtaskflow.domain.comment.dto.response.CommentResponseDto;
import github.npcamp.teamtaskflow.domain.comment.dto.response.CommentResponseListDto;
import github.npcamp.teamtaskflow.domain.comment.exception.CommentException;
import github.npcamp.teamtaskflow.domain.comment.repository.CommentRepository;
import github.npcamp.teamtaskflow.domain.common.entity.Comment;
import github.npcamp.teamtaskflow.domain.common.entity.Task;
import github.npcamp.teamtaskflow.domain.common.entity.User;
import github.npcamp.teamtaskflow.domain.task.service.TaskService;
import github.npcamp.teamtaskflow.domain.user.service.UserService;
import github.npcamp.teamtaskflow.global.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;

    private final TaskService taskService;
    private final UserService userService;

    //댓글 생성
    @Override
    @Transactional
    public CommentResponseDto createComment(Long taskId, Long userId, CreateCommentRequestDto requestDto) {

        Task task = taskService.findTaskByIdOrElseThrow(taskId);
        User user = userService.findUserByIdOrElseThrow(userId);

        Comment comment = Comment.builder()
                .task(task)
                .user(user)
                .content(requestDto.content())
                .build();

        Comment saved = commentRepository.save(comment);

        return CommentResponseDto.toDto(saved);
    }


    //전체조회
    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponseListDto> getComments(Long taskId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findAllByTaskId(taskId, pageable);

        return comments.map(CommentResponseListDto::toDto);
    }

    //댓글 수정
    @Override
    @Transactional
    public CommentResponseDto updateContent(Long taskId, Long commentId, Long userId, UpdateCommentRequestDto requestDto) {

        Task task = taskService.findTaskByIdOrElseThrow(taskId);
        Comment comment = findCommentByIdOrElseThrow(commentId);

        if (!comment.getTask().getId().equals(task.getId())) {
            throw new CommentException(ErrorCode.TASK_COMMENT_MISMATCH);
        }

        User user = userService.findUserByIdOrElseThrow(userId);
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CommentException(ErrorCode.UNAUTHORIZED_COMMENT_ACCESS);
        }

        comment.updateComment(requestDto.content());

        return CommentResponseDto.toDto(comment);
    }

    @Override
    @Transactional
    public CommentDeleteResponseDto deleteComment(Long taskId, Long commentId, Long userId) {

        // Task 존재 확인
        Task task = taskService.findTaskByIdOrElseThrow(taskId);

        // Comment 존재 확인
        Comment comment = findCommentByIdOrElseThrow(commentId);

        if (!comment.getTask().getId().equals(task.getId())) {
            throw new CommentException(ErrorCode.TASK_COMMENT_MISMATCH);
        }

        User user = userService.findUserByIdOrElseThrow(userId);
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CommentException(ErrorCode.UNAUTHORIZED_COMMENT_ACCESS);
        }

        commentRepository.delete(comment);

        return CommentDeleteResponseDto.toDto(comment);
    }

    //commentId 찾는 메서드
    @Override
    @Transactional(readOnly = true)
    public Comment findCommentByIdOrElseThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));
    }
}
