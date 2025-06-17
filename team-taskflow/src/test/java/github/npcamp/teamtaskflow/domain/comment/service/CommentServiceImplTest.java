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
import github.npcamp.teamtaskflow.domain.common.enums.UserRoleEnum;
import github.npcamp.teamtaskflow.domain.task.TaskPriority;
import github.npcamp.teamtaskflow.domain.task.service.TaskService;
import github.npcamp.teamtaskflow.global.exception.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskService taskService;

    private User mockUser() {
        User user = new User("uid1", "tester", "pw", "email", UserRoleEnum.USER);
        ReflectionTestUtils.setField(user, "id", 1L);
        return user;
    }

    private Task mockTask(User user, Long taskId) {
        Task task = Task.builder()
                .title("좋은 태스크")
                .content("좋은 아이디어")
                .priority(TaskPriority.HIGH)
                .dueDate(LocalDateTime.now().plusDays(2))
                .assignee(user)
                .build();
        ReflectionTestUtils.setField(task, "id", taskId);
        return task;
    }

    @Test
    void 댓글_생성_성공() {
        Long taskId = 1L;
        String content = "댓글입니다.";
        CreateCommentRequestDto requestDto = new CreateCommentRequestDto(content);

        User user = mockUser();
        Task task = mockTask(user, taskId);

        Comment comment = Comment.builder()
                .id(100L)
                .task(task)
                .user(user)
                .username(user.getUsername())
                .content(content)
                .build();

        given(taskService.findTaskByIdOrElseThrow(taskId)).willReturn(task);
        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        CommentResponseDto response = commentService.createComment(taskId, requestDto);

        assertNotNull(response);
        assertEquals(content, response.content());
        assertEquals(user.getUsername(), response.userName());
    }

    @Test
    void 댓글_수정_성공() {
        Long taskId = 1L;
        Long commentId = 10L;
        String newContent = "수정된 댓글";
        UpdateCommentRequestDto dto = new UpdateCommentRequestDto(newContent);

        User user = mockUser();
        Task task = mockTask(user, taskId);
        Comment comment = Comment.builder()
                .id(commentId)
                .task(task)
                .user(user)
                .content("이전 댓글")
                .build();

        given(taskService.findTaskByIdOrElseThrow(taskId)).willReturn(task);
        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        CommentResponseDto response = commentService.updateContent(taskId, commentId, dto);

        assertNotNull(response);
        assertEquals(newContent, response.content());
    }

    @Test
    void 댓글_수정_실패_댓글없음() {
        Long taskId = 1L;
        Long commentId = 999L;
        UpdateCommentRequestDto dto = new UpdateCommentRequestDto("수정 시도");
        Task dummyTask = Task.builder().title("dummy").content("dummy").build();

        given(taskService.findTaskByIdOrElseThrow(taskId)).willReturn(dummyTask);
        given(commentRepository.findById(commentId)).willReturn(Optional.empty());

        assertThrows(CommentException.class,
                () -> commentService.updateContent(taskId, commentId, dto));
    }

    @Test
    void 댓글_조회_성공() {
        Long taskId = 1L;
        User user = mockUser();
        Task task = mockTask(user, taskId);
        Comment comment = Comment.builder()
                .id(101L)
                .task(task)
                .user(user)
                .content("조회용 댓글")
                .build();

        Page<Comment> commentPage = new PageImpl<>(List.of(comment));

        given(commentRepository.findAllByTaskId(eq(taskId), any(Pageable.class))).willReturn(commentPage);

        Page<CommentResponseListDto> result = commentService.getComments(taskId, Pageable.ofSize(10));

        assertEquals(1, result.getTotalElements());
        assertEquals("조회용 댓글", result.getContent().get(0).getContent());
    }

    @Test
    void 댓글_조회_실패_없음() {
        Long taskId = 1L;
        Page<Comment> emptyPage = Page.empty();

        given(commentRepository.findAllByTaskId(eq(taskId), any(Pageable.class))).willReturn(emptyPage);

        assertThrows(CommentException.class,
                () -> commentService.getComments(taskId, Pageable.ofSize(10)));
    }

    @Test
    void 댓글_삭제_성공() {
        Long taskId = 1L;
        Long commentId = 10L;

        User user = mockUser();
        Task task = mockTask(user, taskId);
        Comment comment = Comment.builder()
                .id(commentId)
                .task(task)
                .user(user)
                .content("삭제할 댓글")
                .build();

        given(taskService.findTaskByIdOrElseThrow(taskId)).willReturn(task);
        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        CommentDeleteResponseDto response = commentService.deleteComment(taskId, commentId);

        assertNotNull(response);
        assertTrue(response.isDeleted());
        assertEquals(commentId, response.id());
    }

    @Test
    void 댓글_삭제_실패_매칭안됨() {
        Long taskId = 1L;
        Long commentId = 10L;

        User user = mockUser();
        Task realTask = mockTask(user, taskId);
        Task otherTask = mockTask(user, 99L);

        Comment comment = Comment.builder()
                .id(commentId)
                .task(otherTask)
                .user(user)
                .content("삭제할 댓글")
                .build();

        given(taskService.findTaskByIdOrElseThrow(taskId)).willReturn(realTask);
        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        assertThrows(CommentException.class,
                () -> commentService.deleteComment(taskId, commentId));
    }
}
