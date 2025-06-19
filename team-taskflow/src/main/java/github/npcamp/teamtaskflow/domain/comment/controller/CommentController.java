package github.npcamp.teamtaskflow.domain.comment.controller;

import github.npcamp.teamtaskflow.domain.comment.dto.request.CreateCommentRequestDto;
import github.npcamp.teamtaskflow.domain.comment.dto.request.UpdateCommentRequestDto;
import github.npcamp.teamtaskflow.domain.comment.dto.response.CommentDetailDto;
import github.npcamp.teamtaskflow.domain.comment.dto.response.CommentPageDto;
import github.npcamp.teamtaskflow.domain.comment.service.CommentService;
import github.npcamp.teamtaskflow.domain.common.aop.Logging;
import github.npcamp.teamtaskflow.domain.log.ActivityType;
import github.npcamp.teamtaskflow.global.payload.ApiResponse;
import github.npcamp.teamtaskflow.global.payload.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //댓글생성
    @PostMapping
    @Logging(ActivityType.COMMENT_CREATED)
    public ResponseEntity<ApiResponse<CommentDetailDto>> createComment(
            @PathVariable Long taskId,
            @Valid @RequestBody CreateCommentRequestDto requestDto,
            @AuthenticationPrincipal Long currentUserId
    ) {
        CommentDetailDto responseDto = commentService.createComment(taskId, currentUserId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(responseDto));
    }

    //댓글 전체 조회
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CommentDetailDto>>> getComments(
            @PathVariable Long taskId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
//        Page<CommentDetailDto> page = commentService.getComments(taskId, pageable);
//        PageResponse<CommentDetailDto> responseDto = new PageResponse<>(page);
        PageResponse<CommentDetailDto> responseDto =commentService.getComments(taskId, pageable);
//        return ResponseEntity.ok(ApiResponse.success("댓글 목록을 조회했습니다.", responseDto));
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("댓글이 성공적으로 조회되었습니다.", responseDto));    }

    //댓글 수정 - content만 수정하므로, patch사용함.
    @PatchMapping("/{commentId}")
    @Logging(ActivityType.COMMENT_UPDATED)
    public ResponseEntity<ApiResponse<CommentDetailDto>> updateComment(
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @Valid @RequestBody UpdateCommentRequestDto requestDto,
            @AuthenticationPrincipal Long currentUserId
    ) {
        CommentDetailDto responseDto = commentService.updateContent(taskId, commentId, currentUserId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(responseDto));
    }

    //댓글삭제 (soft delete)
    @DeleteMapping("/{commentId}")
    @Logging(ActivityType.COMMENT_DELETED)
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal Long currentUserId
    ) {
        commentService.deleteComment(taskId, commentId,currentUserId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("댓글이 성공적으로 삭제되었습니다."));
    }
}
