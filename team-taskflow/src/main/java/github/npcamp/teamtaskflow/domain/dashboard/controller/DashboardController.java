package github.npcamp.teamtaskflow.domain.dashboard.controller;

import github.npcamp.teamtaskflow.domain.dashboard.dto.response.TaskCompletionResponseDto;
import github.npcamp.teamtaskflow.domain.dashboard.dto.response.TaskStatusResponseDto;
import github.npcamp.teamtaskflow.domain.dashboard.dto.response.TodayMyTaskListResponseDto;
import github.npcamp.teamtaskflow.domain.dashboard.dto.response.TotalTaskResponseDto;
import github.npcamp.teamtaskflow.domain.dashboard.service.DashboardService;
import github.npcamp.teamtaskflow.global.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;


     // 삭제되지 않은 시스템의 존재하는 모든 태스크의 총 개수 표시
    @GetMapping("/tasks")
    public ResponseEntity<ApiResponse<TotalTaskResponseDto>> getTotalTasks(){
        TotalTaskResponseDto dto = dashboardService.getTotalTasks();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(dto));
    }

    // 상태별 태스크 수 조회
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<List<TaskStatusResponseDto>>> getStatusTasks(){
        List<TaskStatusResponseDto> dto = dashboardService.getStatusTasks();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(dto));
    }

    //전체 태스크 대비 완료율 조회
    @GetMapping("/completion")
    public ResponseEntity<ApiResponse<TaskCompletionResponseDto>> getCompletion(){
        TaskCompletionResponseDto dto = dashboardService.getCompletion();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(dto));
    }

    //오늘 내 태스크 목록 조회
    @GetMapping("/today")
    public ResponseEntity<ApiResponse<List<TodayMyTaskListResponseDto>>> getTodayMyTask(@AuthenticationPrincipal Long userId){
        List<TodayMyTaskListResponseDto> dto = dashboardService.getTodayMyTask(userId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(dto));
    }
}
