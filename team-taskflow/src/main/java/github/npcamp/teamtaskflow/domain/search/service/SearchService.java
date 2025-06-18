package github.npcamp.teamtaskflow.domain.search.service;

import github.npcamp.teamtaskflow.domain.comment.dto.response.CommentDetailDto;
import github.npcamp.teamtaskflow.domain.comment.dto.response.CommentPageDto;
import github.npcamp.teamtaskflow.domain.task.dto.response.TaskResponseDto;
import github.npcamp.teamtaskflow.global.payload.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchService {
    Page<TaskResponseDto> searchTasks(String keyword, Pageable pageable);
    PageResponse<CommentDetailDto> searchComments(String keyword, Pageable pageable);
}