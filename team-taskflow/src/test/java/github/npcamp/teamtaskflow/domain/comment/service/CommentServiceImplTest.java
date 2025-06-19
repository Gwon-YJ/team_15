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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskService taskService;

    @Mock
    private UserService userService;

    @Test
    void createComment_성공() {
        // given: 유효한 taskId와 userId, 댓글 내용 준비
        Long taskId = 1L;
        Long userId = 2L;
        String content = "테스트 댓글입니다";

        Task task = Task.builder().id(taskId).title("할 일").build();
        User user = User.builder().id(userId).username("tester").build();
        Comment savedComment = Comment.builder()
                .id(10L)
                .task(task)
                .user(user)
                .content(content)
                .build();

        // when: taskService, userService, commentRepository 동작 정의
        when(taskService.findTaskByIdOrElseThrow(taskId)).thenReturn(task);
        when(userService.findUserByIdOrElseThrow(userId)).thenReturn(user);
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        CreateCommentRequestDto requestDto = new CreateCommentRequestDto(content);

        // when: 댓글 생성 요청
        CommentDetailDto response = commentService.createComment(taskId, userId, requestDto);

        // then: 생성된 댓글의 정보가 예상값과 일치하는지 검증
        assertEquals(10L, response.id());
        assertEquals(content, response.content());
        assertEquals(userId, response.userId());
        assertEquals(taskId, response.taskId());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    // Task가 존재하지 않음 -> TASK_NOT_FOUND
    @Test
    void createComment_실패_Task없음() {
        // given: 존재하지 않는 taskId 사용
        Long taskId = 1L;
        Long userId = 2L;
        CreateCommentRequestDto requestDto = new CreateCommentRequestDto("내용");

        // when: taskService에서 예외 발생하도록 설정
        when(taskService.findTaskByIdOrElseThrow(taskId))
                .thenThrow(new CommentException(ErrorCode.TASK_NOT_FOUND));

        // then: 예외 발생 검증
        CommentException ex = assertThrows(CommentException.class, () ->
                commentService.createComment(taskId, userId, requestDto)
        );
        assertEquals(ErrorCode.TASK_NOT_FOUND, ex.getErrorCode());
    }

    // 존재하지 않는 USER → USER_NOT_FOUND
    @Test
    void createComment_실패_User없음() {
        // given: 유효한 taskId와 존재하지 않는 userId 사용
        Long taskId = 1L;
        Long userId = 2L;
        Task task = Task.builder().id(taskId).title("할 일").build();
        CreateCommentRequestDto requestDto = new CreateCommentRequestDto("내용");

        // when: taskService는 정상 동작, userService에서 예외 발생 설정
        when(taskService.findTaskByIdOrElseThrow(taskId)).thenReturn(task);
        when(userService.findUserByIdOrElseThrow(userId))
                .thenThrow(new CommentException(ErrorCode.USER_NOT_FOUND));

        // then: 예외 발생 검증
        CommentException ex = assertThrows(CommentException.class, () ->
                commentService.createComment(taskId, userId, requestDto)
        );
        assertEquals(ErrorCode.USER_NOT_FOUND, ex.getErrorCode());
    }

    //댓글 작성자가 자기 댓글을 정상 수정
    @Test
    void updateContent_성공() {
        // given: 유효한 task, user, comment와 수정할 내용 설정
        Long taskId = 1L;
        Long commentId = 10L;
        Long userId = 2L;
        String newContent = "수정된 댓글입니다.";

        Task task = Task.builder().id(taskId).title("할 일").build();
        User user = User.builder().id(userId).username("tester").build();
        Comment comment = Comment.builder().id(commentId).task(task).user(user).content("기존 댓글").build();

        when(taskService.findTaskByIdOrElseThrow(taskId)).thenReturn(task);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(userService.findUserByIdOrElseThrow(userId)).thenReturn(user);

        UpdateCommentRequestDto requestDto = new UpdateCommentRequestDto(newContent);

        // when: 댓글 수정 요청
        CommentDetailDto response = commentService.updateContent(taskId, commentId, userId, requestDto);

        // then
        assertEquals(commentId, response.id());
        assertEquals(newContent, response.content());
        verify(commentRepository, never()).save(any()); // 내부에서 save 호출 없이 dirty checking
    }

    // then: 수정된 내용이 반영되고 save는 호출되지 않음
    @Test
    void updateContent_실패_Task없음() {
        // given: 존재하지 않는 taskId 사용
        Long taskId = 1L;
        Long commentId = 10L;
        Long userId = 2L;
        UpdateCommentRequestDto dto = new UpdateCommentRequestDto("수정");

        // when: taskService에서 예외 발생 설정
        when(taskService.findTaskByIdOrElseThrow(taskId))
                .thenThrow(new CommentException(ErrorCode.TASK_NOT_FOUND));

        // then: 예외 발생 검증
        assertThrows(CommentException.class, () ->
                commentService.updateContent(taskId, commentId, userId, dto)
        );
    }

    //Comment가 존재하지 않음 -> COMMENT_NOT_FOUND
    @Test
    void updateContent_실패_Comment없음() {
        // given: 존재하지 않는 commentId 사용
        Long taskId = 1L;
        Long commentId = 10L;
        Long userId = 2L;
        UpdateCommentRequestDto dto = new UpdateCommentRequestDto("수정");

        Task task = Task.builder().id(taskId).build();

        // when: commentRepository에서 예외 발생 설정
        when(taskService.findTaskByIdOrElseThrow(taskId)).thenReturn(task);
        when(commentRepository.findById(commentId))
                .thenThrow(new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        // then: 예외 발생 검증
        assertThrows(CommentException.class, () ->
                commentService.updateContent(taskId, commentId, userId, dto)
        );
    }

    // Comment가 해당 Task에 속하지 않음 -> TASK_COMMENT_MISMATCH
    @Test
    void updateContent_실패_Task_Comment_불일치() {
        // given: comment가 다른 task에 속함
        Long taskId = 1L;
        Long commentId = 10L;
        Long userId = 2L;
        UpdateCommentRequestDto dto = new UpdateCommentRequestDto("수정");

        Task task1 = Task.builder().id(taskId).build(); // 요청에서 사용한 task
        Task task2 = Task.builder().id(99L).build(); // 댓글에 실제 연결된 task
        User user = User.builder().id(userId).build();
        Comment comment = Comment.builder().id(commentId).task(task2).user(user).content("기존").build();

        // when: comment의 task와 요청한 task가 불일치
        when(taskService.findTaskByIdOrElseThrow(taskId)).thenReturn(task1);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        assertThrows(CommentException.class, () ->
                commentService.updateContent(taskId, commentId, userId, dto)
        );
    }

    // 요청한 User가 작성자가 아님 -> UNAUTHORIZED_COMMENT_ACCESS
    @Test
    void updateContent_실패_작성자_불일치() {
        // given: 요청자와 댓글 작성자가 다름
        Long taskId = 1L;
        Long commentId = 10L;
        Long userId = 2L; // 요청자
        Long authorId = 3L; // 실제 댓글 작성자

        UpdateCommentRequestDto dto = new UpdateCommentRequestDto("수정");

        Task task = Task.builder().id(taskId).build();
        User actualAuthor = User.builder().id(authorId).build();
        User requestUser = User.builder().id(userId).build();
        Comment comment = Comment.builder().id(commentId).task(task).user(actualAuthor).content("기존").build();

        // when: 요청자가 댓글 작성자가 아님
        when(taskService.findTaskByIdOrElseThrow(taskId)).thenReturn(task);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(userService.findUserByIdOrElseThrow(userId)).thenReturn(requestUser);

        // then: 예외 발생 검증
        assertThrows(CommentException.class, () ->
                commentService.updateContent(taskId, commentId, userId, dto)
        );
    }

    // 본인이 작성한 댓글을 정상적으로 삭제함
    @Test
    void deleteComment_성공() {
        // given: 유효한 task, user, comment 준비 (댓글 작성자 본인)
        Long taskId = 1L;
        Long commentId = 10L;
        Long userId = 2L;

        Task task = Task.builder().id(taskId).build();
        User user = User.builder().id(userId).build();
        Comment comment = Comment.builder().id(commentId).task(task).user(user).content("댓글").build();

        when(taskService.findTaskByIdOrElseThrow(taskId)).thenReturn(task);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(userService.findUserByIdOrElseThrow(userId)).thenReturn(user);

        // when: 댓글 삭제 요청
        commentService.deleteComment(taskId, commentId, userId);

        // then: 삭제 메서드가 한 번 호출되었는지 검증
        verify(commentRepository, times(1)).delete(comment);
    }

    // Task가 존재하지 않음 -> TASK_NOT_FOUND
    @Test
    void deleteComment_실패_Task없음() {
        // given: 존재하지 않는 taskId 사용
        Long taskId = 1L;
        Long commentId = 10L;
        Long userId = 2L;

        // when: taskService에서 예외 발생 설정
        when(taskService.findTaskByIdOrElseThrow(taskId))
                .thenThrow(new CommentException(ErrorCode.TASK_NOT_FOUND));

        // then: 예외 발생 검증
        assertThrows(CommentException.class, () ->
                commentService.deleteComment(taskId, commentId, userId)
        );
    }

    // Comment가 존재하지 않음 -> COMMENT_NOT_FOUND
    @Test
    void deleteComment_실패_Comment없음() {
        // given: 존재하지 않는 commentId 사용
        Long taskId = 1L;
        Long commentId = 10L;
        Long userId = 2L;

        Task task = Task.builder().id(taskId).build();

        // when: commentRepository에서 예외 발생 설정
        when(taskService.findTaskByIdOrElseThrow(taskId)).thenReturn(task);
        when(commentRepository.findById(commentId))
                .thenThrow(new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        // then: 예외 발생 검증
        assertThrows(CommentException.class, () ->
                commentService.deleteComment(taskId, commentId, userId)
        );
    }

    // Comment가 Task에 속하지 않음 -> TASK_COMMENT_MISMATCH
    @Test
    void deleteComment_실패_Task_Comment_불일치() {
        // given: comment의 task와 요청 task가 다름
        Long taskId = 1L;
        Long commentId = 10L;
        Long userId = 2L;

        Task task1 = Task.builder().id(taskId).build();
        Task task2 = Task.builder().id(99L).build();
        User user = User.builder().id(userId).build();
        Comment comment = Comment.builder().id(commentId).task(task2).user(user).content("댓글").build();

        // when: task와 comment의 task 불일치 설정
        when(taskService.findTaskByIdOrElseThrow(taskId)).thenReturn(task1);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // then: 예외 발생 검증
        assertThrows(CommentException.class, () ->
                commentService.deleteComment(taskId, commentId, userId)
        );
    }

    // 댓글 작성자가 아님 -> UNAUTHORIZED_COMMENT_ACCESS
    @Test
    void deleteComment_실패_작성자_불일치() {
        // given: 요청자와 댓글 작성자가 다름
        Long taskId = 1L;
        Long commentId = 10L;
        Long userId = 2L;
        Long realAuthorId = 3L;

        Task task = Task.builder().id(taskId).build();
        User realAuthor = User.builder().id(realAuthorId).build();
        User requester = User.builder().id(userId).build();
        Comment comment = Comment.builder().id(commentId).task(task).user(realAuthor).content("댓글").build();

        // when: 요청자가 댓글 작성자가 아닌 상황 설정
        when(taskService.findTaskByIdOrElseThrow(taskId)).thenReturn(task);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(userService.findUserByIdOrElseThrow(userId)).thenReturn(requester);

        // then: 예외 발생 검증
        assertThrows(CommentException.class, () ->
                commentService.deleteComment(taskId, commentId, userId)
        );
    }

    @Test
    void getComments_성공() {
        // given: 특정 taskId에 대해 2개의 댓글이 존재함
        Long taskId = 1L;
        Pageable pageable = PageRequest.of(0, 2);

        Comment comment1 = Comment.builder()
                .id(1L)
                .content("첫 번째 댓글")
                .user(User.builder().username("user1").build())
                .task(Task.builder().id(taskId).build())
                .build();

        Comment comment2 = Comment.builder()
                .id(2L)
                .content("두 번째 댓글")
                .user(User.builder().username("user2").build())
                .task(Task.builder().id(taskId).build())
                .build();

        Page<Comment> commentPage = new PageImpl<>(List.of(comment1, comment2), pageable, 2);
        when(commentRepository.findAllByTaskId(taskId, pageable)).thenReturn(commentPage);

        // when: 댓글 목록 조회 요청
        PageResponse<CommentDetailDto> result = commentService.getComments(taskId, pageable);
        // then: 반환된 목록의 크기 및 첫 번째 댓글 내용 검증, repository 호출 확인
        assertEquals(2, result.getContent().size());
        assertEquals("첫 번째 댓글", result.getContent().get(0).content());
        verify(commentRepository, times(1)).findAllByTaskId(taskId, pageable);
    }

    @Test
    void findCommentByIdOrElseThrow_성공() {
        // given: 존재하는 commentId에 대한 댓글이 존재함
        Long commentId = 1L;
        Comment comment = Comment.builder().id(commentId).content("댓글 내용").build();
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // when: 댓글 단건 조회 요청
        Comment result = commentService.findCommentByIdOrElseThrow(commentId);

        // then: 반환된 댓글의 ID와 내용이 예상과 일치하는지 검증
        assertEquals(commentId, result.getId());
        assertEquals("댓글 내용", result.getContent());
        verify(commentRepository, times(1)).findById(commentId);
    }

    @Test
    void findCommentByIdOrElseThrow_실패_존재하지않음() {
        // given: 존재하지 않는 commentId 사용
        Long commentId = 1L;
        when(commentRepository.findById(commentId))
                .thenThrow(new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        // when & then: 예외 발생 검증
        assertThrows(CommentException.class, () -> {
            commentService.findCommentByIdOrElseThrow(commentId);
        });
    }
}