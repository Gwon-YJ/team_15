package github.npcamp.teamtaskflow.domain.comment.controller;

import github.npcamp.teamtaskflow.domain.comment.dto.request.CreateCommentRequestDto;
import github.npcamp.teamtaskflow.domain.comment.dto.request.UpdateCommentRequestDto;
import github.npcamp.teamtaskflow.domain.comment.dto.response.CommentDeleteResponseDto;
import github.npcamp.teamtaskflow.domain.comment.dto.response.CommentResponseDto;
import github.npcamp.teamtaskflow.domain.comment.dto.response.CommentResponseListDto;
import github.npcamp.teamtaskflow.domain.comment.service.CommentService;
import github.npcamp.teamtaskflow.global.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/tasks/{taskId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponseDto>> createComment(
            @PathVariable Long taskId,
            @Valid @RequestBody CreateCommentRequestDto requestDto
    ) {
        CommentResponseDto responseDto = commentService.createComment(taskId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(responseDto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CommentResponseListDto>>> getComments(
            @PathVariable Long taskId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<CommentResponseListDto> responseDto = commentService.getComments(taskId, pageable);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(responseDto));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> updateComment(
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @Valid @RequestBody UpdateCommentRequestDto requestDto
    ) {
        CommentResponseDto responseDto = commentService.updateContent(taskId, commentId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(responseDto));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentDeleteResponseDto>> deleteComment(
            @PathVariable Long taskId,
            @PathVariable Long commentId
    ) {
        CommentDeleteResponseDto responseDto = commentService.deleteComment(taskId, commentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("댓글이 성공적으로 삭제되었습니다."));
    }
}
