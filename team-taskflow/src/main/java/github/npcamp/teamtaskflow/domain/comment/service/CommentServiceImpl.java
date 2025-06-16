package github.npcamp.teamtaskflow.domain.comment.service;

import github.npcamp.teamtaskflow.domain.comment.dto.request.CreateCommentRequestDto;
import github.npcamp.teamtaskflow.domain.comment.dto.request.UpdateCommentRequestDto;
import github.npcamp.teamtaskflow.domain.comment.dto.response.CommentResponseDto;
import github.npcamp.teamtaskflow.domain.comment.dto.response.CommentResponseListDto;
import github.npcamp.teamtaskflow.domain.comment.exception.CommentNotFoundException;
import github.npcamp.teamtaskflow.domain.comment.repository.CommentRepository;
import github.npcamp.teamtaskflow.domain.common.entity.Comment;
import github.npcamp.teamtaskflow.domain.common.entity.Task;
import github.npcamp.teamtaskflow.domain.common.entity.User;
import github.npcamp.teamtaskflow.domain.common.enums.UserRoleEnum;
import github.npcamp.teamtaskflow.domain.task.exception.TaskNotFoundException;
import github.npcamp.teamtaskflow.domain.task.repository.TaskRepository;
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
    private final TaskRepository taskRepository;
//    private final UserRepository userRepository;

    //댓글 생성
    @Override
    @Transactional
    public CommentResponseDto createComment(Long taskId,  CreateCommentRequestDto requestDto) {

//        Todo: Task task = taskService.findByIdOrElseThrow(taskId);

        // task 조회 (예외 처리 포함)
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(ErrorCode.TASK_NOT_FOUND));

//        Todo: User user = userRepository.findByUsername(userId)
//                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        Comment comment = Comment.builder()
                .task(task)
//Todo:                .user(user)
                .content(requestDto.content())
                .build();

        Comment saved = commentRepository.save(comment);

        return CommentResponseDto.toDto(saved);
    }

    //commentId 찾는 메서드
    @Override
    @Transactional(readOnly = true)
    public Comment findByIdOrElseThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(ErrorCode.COMMENT_NOT_FOUND));
    }

    //전체조회
    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponseListDto> getComments(Long taskId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findAllByTaskId(taskId, pageable);

        return comments.map(CommentResponseListDto::toDto);
    }

    @Override
    @Transactional
    public CommentResponseDto updateContent(Long taskId, Long commentId, UpdateCommentRequestDto requestDto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(ErrorCode.TASK_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(ErrorCode.COMMENT_NOT_FOUND));

        // task 연관 검사 (선택)
        if (!comment.getTask().getId().equals(task.getId())) {
            throw new CommentNotFoundException(ErrorCode.TASK_COMMENT_MISMATCH);
        }

        comment.updateComment(requestDto.content());

        return CommentResponseDto.toDto(comment);
    }
}
