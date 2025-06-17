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
import github.npcamp.teamtaskflow.domain.task.TaskPriority;
import github.npcamp.teamtaskflow.domain.task.repository.TaskRepository;
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
    private TaskRepository taskRepository;

    @Test
    void 댓글_생성_성공() {
        Long taskId = 1L;
        String content = "댓글입니다.";
        String username = "tester1";

        CreateCommentRequestDto requestDto = new CreateCommentRequestDto(content);

        User user = new User(
                "uid1",
                username,
                "pw",
                "test@email.com",
                UserRoleEnum.USER);
        ReflectionTestUtils.setField(user, "id", 1L);

        Task task = Task.builder()
                .title("제목")
                .content("내용")
                .priority(TaskPriority.LOW)
                .dueDate(LocalDateTime.now().plusDays(3))
                .assignee(user)
                .build();
        ReflectionTestUtils.setField(task, "id", taskId);

        Comment comment = Comment.builder()
                .id(100L)
                .task(task)
                .user(user)
                .content(content)
                .build();

        given(taskRepository.findById(taskId)).willReturn(Optional.of(task));
        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        CommentResponseDto response = commentService.createComment(taskId, requestDto);

        assertNotNull(response);
        assertEquals(content, response.content());
        assertEquals(username, response.userName());
    }

    // 댓글 수정 성공
    @Test
    void 댓글_수정_성공() {
        Long taskId = 1L;
        Long commentId = 10L;
        String newContent = "수정된 댓글";

        UpdateCommentRequestDto dto = new UpdateCommentRequestDto(newContent);

        User user = new User("uid1", "tester", "pw", "email", UserRoleEnum.USER);
        ReflectionTestUtils.setField(user, "id", 1L);

        Task task = Task.builder()
                .title("테스트")
                .content("내용")
                .priority(TaskPriority.HIGH)
                .dueDate(LocalDateTime.now().plusDays(1))
                .assignee(user)
                .build();
        ReflectionTestUtils.setField(task, "id", taskId);

        Comment comment = Comment.builder()
                .id(commentId)
                .task(task)
                .user(user)
                .content("이전 댓글")
                .build();

        given(taskRepository.findById(taskId)).willReturn(Optional.of(task));
        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        CommentResponseDto response = commentService.updateContent(taskId, commentId, dto);

        assertNotNull(response);
        assertEquals(newContent, response.content());
    }

    //  댓글 수정 실패
    @Test
    void 댓글_수정_실패_댓글없음() {
        Long taskId = 1L;
        Long commentId = 999L;
        UpdateCommentRequestDto dto = new UpdateCommentRequestDto("수정 시도");

        Task dummyTask = Task.builder().title("dummy").content("dummy").build();

        given(taskRepository.findById(taskId)).willReturn(Optional.of(dummyTask));
        given(commentRepository.findById(commentId)).willReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class,
                () -> commentService.updateContent(taskId, commentId, dto));
    }

    // 댓글 조회 성공
    @Test
    void 댓글_조회_성공() {
        Long taskId = 1L;

        User user = new User(
                "uid1",
                "tester",
                "pw",
                "tester@email.com",
                UserRoleEnum.USER);
        ReflectionTestUtils.setField(user, "id", 1L);

        Task task = Task.builder()
                .title("제목")
                .content("내용")
                .priority(TaskPriority.HIGH)
                .dueDate(LocalDateTime.now().plusDays(2))
                .assignee(user)
                .build();
        ReflectionTestUtils.setField(task, "id", taskId);

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

    //  댓글 조회 실패 - 댓글 없음
    @Test
    void 댓글_조회_실패_없음() {
        Long taskId = 1L;

        Page<Comment> emptyPage = Page.empty();

        given(commentRepository.findAllByTaskId(eq(taskId), any(Pageable.class))).willReturn(emptyPage);

        assertThrows(CommentNotFoundException.class,
                () -> commentService.getComments(taskId, Pageable.ofSize(10)));
    }
}