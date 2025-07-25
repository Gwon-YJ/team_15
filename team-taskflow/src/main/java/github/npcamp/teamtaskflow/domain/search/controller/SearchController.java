package github.npcamp.teamtaskflow.domain.search.controller;

import github.npcamp.teamtaskflow.domain.comment.dto.response.CommentDetailDto;
import github.npcamp.teamtaskflow.domain.search.service.SearchService;
import github.npcamp.teamtaskflow.domain.task.dto.response.TaskResponseDto;
import github.npcamp.teamtaskflow.global.payload.ApiResponse;
import github.npcamp.teamtaskflow.global.payload.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    /**
     * (제목, 내용) 키워드를 입력하여 task 조회
     * 예시: /search/tasks?keyword={검색어}&page=1
     */
    @GetMapping("/tasks")
    public ResponseEntity<ApiResponse<Page<TaskResponseDto>>> searchTasks(@RequestParam String keyword,
                                                                          @PageableDefault(sort = "dueDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TaskResponseDto> tasks = searchService.searchTasks(keyword, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(tasks));
    }

    /**
     * 댓글 키워드 검색
     * 예시: /search/comment?keyword={검색어}&page=1
     */
    @GetMapping("/comments")
    public ResponseEntity<ApiResponse<PageResponse<CommentDetailDto>>> searchComments(
            @RequestParam String keyword,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<CommentDetailDto> responseDto = searchService.searchComments(keyword, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("댓글이 성공적으로 조회되었습니다.", responseDto));
    }

}