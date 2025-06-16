package github.npcamp.teamtaskflow.domain.dashboard.controller;

import github.npcamp.teamtaskflow.domain.dashboard.dto.response.TaskStatusResponseDto;
import github.npcamp.teamtaskflow.domain.dashboard.dto.response.TotalTaskResponseDto;
import github.npcamp.teamtaskflow.domain.dashboard.service.DashboardService;
import github.npcamp.teamtaskflow.global.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ApiResponse<TaskStatusResponseDto>> getStatusTasks(){
        TaskStatusResponseDto dto = dashboardService.getStatusTasks();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(dto));
    }
}
