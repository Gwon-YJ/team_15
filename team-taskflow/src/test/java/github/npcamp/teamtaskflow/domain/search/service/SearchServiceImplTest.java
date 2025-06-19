package github.npcamp.teamtaskflow.domain.search.service;

import github.npcamp.teamtaskflow.domain.comment.dto.response.CommentDetailDto;
import github.npcamp.teamtaskflow.domain.comment.repository.CommentRepository;
import github.npcamp.teamtaskflow.domain.common.entity.Comment;
import github.npcamp.teamtaskflow.domain.common.entity.Task;
import github.npcamp.teamtaskflow.domain.common.entity.User;
import github.npcamp.teamtaskflow.domain.task.repository.TaskRepository;
import github.npcamp.teamtaskflow.global.payload.PageResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceImplTest {


    @Mock
    private CommentRepository commentRepository;
    private TaskRepository taskRepository;

    @InjectMocks
    private SearchServiceImpl searchService;

    // 검색 키워드가 포함된 댓글이 전부 보일 경우 정상 반환
    @Test
    void searchComments_성공() {
        // given: '검색' 키워드를 포함한 댓글 2건과 Page 설정
        String keyword = "검색";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        Comment comment1 = Comment.builder()
                .id(1L)
                .content("검색 관련 댓글")
                .user(User.builder().id(1L).username("tester1").build())
                .task(Task.builder().id(1L).build())
                .build();

        Comment comment2 = Comment.builder()
                .id(2L)
                .content("또 다른 검색 내용")
                .user(User.builder().id(2L).username("tester2").build())
                .task(Task.builder().id(1L).build())
                .build();

        List<Comment> commentList = List.of(comment1, comment2);
        Page<Comment> commentPage = new PageImpl<>(commentList, pageable, commentList.size());

        when(commentRepository.findByContentContainingIgnoreCase(keyword, pageable))
                .thenReturn(commentPage);

        // when: 키워드로 댓글 검색
        PageResponse<CommentDetailDto> result = searchService.searchComments(keyword, pageable);

        // then: 반환된 결과 수와 내용 포함 여부 검증
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().get(0).content().contains("검색"));
        verify(commentRepository, times(1)).findByContentContainingIgnoreCase(keyword, pageable);
    }

    // 검색 결과가 비어 있는 경우
    @Test
    void searchComments_결과없음() {
        // given: 존재하지 않는 키워드 설정 및 빈 페이지 응답
        String keyword = "없는키워드";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(commentRepository.findByContentContainingIgnoreCase(keyword, pageable))
                .thenReturn(emptyPage);

        // when: 키워드로 댓글 검색
        PageResponse<CommentDetailDto> result = searchService.searchComments(keyword, pageable);

        // then: 반환된 결과가 비어있는지 검증
        assertTrue(result.getContent().isEmpty());
        verify(commentRepository, times(1)).findByContentContainingIgnoreCase(keyword, pageable);
    }
}