package github.npcamp.teamtaskflow.domain.comment.service;

import github.npcamp.teamtaskflow.domain.comment.dto.request.CreateCommentRequestDto;
import github.npcamp.teamtaskflow.domain.comment.dto.request.UpdateCommentRequestDto;
import github.npcamp.teamtaskflow.domain.comment.dto.response.CommentDetailDto;
import github.npcamp.teamtaskflow.domain.comment.exception.CommentException;
import github.npcamp.teamtaskflow.domain.comment.repository.CommentRepository;
import github.npcamp.teamtaskflow.domain.common.entity.Comment;
import github.npcamp.teamtaskflow.domain.common.entity.Task;
import github.npcamp.teamtaskflow.domain.common.entity.User;
import github.npcamp.teamtaskflow.domain.task.service.TaskService;
import github.npcamp.teamtaskflow.domain.user.service.UserService;
import github.npcamp.teamtaskflow.global.exception.ErrorCode;
import github.npcamp.teamtaskflow.global.payload.PageResponse;
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
    public CommentDetailDto createComment(Long taskId, Long userId, CreateCommentRequestDto requestDto) {

        Task task = taskService.findTaskByIdOrElseThrow(taskId); // 태스크 존재 여부 확인
        User user = userService.findUserByIdOrElseThrow(userId); // 사용자 존재 여부 확인

        Comment comment = Comment.builder() // 댓글 객체 생성
                .task(task)
                .user(user)
                .content(requestDto.content())
                .build();

        Comment saved = commentRepository.save(comment); // 댓글 저장

        return CommentDetailDto.toDto(saved); // 응답 DTO 반환
    }


    //전체조회
    @Override
    @Transactional(readOnly = true)
    public PageResponse<CommentDetailDto> getComments(Long taskId, Pageable pageable) {
        Page<CommentDetailDto> comments = commentRepository.findAllByTaskId(taskId, pageable)
                .map(CommentDetailDto::toDto);

        return new PageResponse<>(comments);
    }

    //댓글 수정
    @Override
    @Transactional
    public CommentDetailDto updateContent(Long taskId, Long commentId, Long userId, UpdateCommentRequestDto requestDto) {

        Task task = taskService.findTaskByIdOrElseThrow(taskId);
        Comment comment = findCommentByIdOrElseThrow(commentId);

        // 댓글이 해당 task에 속하지 않는 경우 예외처리
        if (!comment.getTask().getId().equals(task.getId())) {
            throw new CommentException(ErrorCode.TASK_COMMENT_MISMATCH);
        }

        User user = userService.findUserByIdOrElseThrow(userId); // 사용자 존재 확인
        if (!comment.getUser().getId().equals(user.getId())) { // 댓글 본인 확인 - 아니면 에러코드
            throw new CommentException(ErrorCode.UNAUTHORIZED_COMMENT_ACCESS);
        }

        comment.updateComment(requestDto.content()); // 댓글 내용 수정

        commentRepository.flush();

        return CommentDetailDto.toDto(comment); // 수정된 댓글 반환
    }

    //댓글 삭제
    @Override
    @Transactional
    public void deleteComment(Long taskId, Long commentId, Long userId) {

        Task task = taskService.findTaskByIdOrElseThrow(taskId);
        Comment comment = findCommentByIdOrElseThrow(commentId);

        if (!comment.getTask().getId().equals(task.getId())) {
            throw new CommentException(ErrorCode.TASK_COMMENT_MISMATCH);
        }

        User user = userService.findUserByIdOrElseThrow(userId);
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CommentException(ErrorCode.UNAUTHORIZED_COMMENT_ACCESS);
        }

        commentRepository.delete(comment); // 댓글 내용 삭제 (soft)
    }

    // commentId로 댓글을 조회하고 없으면 예외
    @Override
    @Transactional(readOnly = true)
    public Comment findCommentByIdOrElseThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));
    }
}
