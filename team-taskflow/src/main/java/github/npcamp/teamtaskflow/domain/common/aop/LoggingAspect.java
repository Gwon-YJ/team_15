package github.npcamp.teamtaskflow.domain.common.aop;

import github.npcamp.teamtaskflow.domain.auth.dto.response.AuthResponseDto;
import github.npcamp.teamtaskflow.domain.common.base.Identifiable;
import github.npcamp.teamtaskflow.domain.log.ActivityType;
import github.npcamp.teamtaskflow.domain.log.service.ActivityLogService;
import github.npcamp.teamtaskflow.domain.task.TaskStatus;
import github.npcamp.teamtaskflow.domain.task.dto.request.UpdateStatusRequestDto;
import github.npcamp.teamtaskflow.global.payload.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LoggingAspect {

    private final ActivityLogService activityLogService;

    /**
     *
     * @Around 어노테이션으로 AOP 로깅 처리
     * @param joinPoint - 실제 실행될 메서드 정보
     * @param logging - 각 메서드에 선언된 @Logging 어노테이션
     */
    @Around("@annotation(logging)")
    public Object logApi(ProceedingJoinPoint joinPoint, Logging logging) throws Throwable{

        HttpServletRequest request = getCurrentRequest();
        if (request == null){
            return joinPoint.proceed();
        }

        String ip = request.getRemoteAddr();
        String method = request.getMethod();
        String url = request.getRequestURI();

        // 어떤 활동을 로깅 할지 결정 (어노테이션에 명시됨) ex => @Logging(ActivityType.XXX)
        ActivityType activityType = logging.value();

        Object[] args = joinPoint.getArgs(); // 메서드 인자들
        Object result; // 메서드 실행 결과

        String message = null; // 로그에 기록할 메세지
        Long userId = null;
        Long targetId = null; // 대상 엔티티 ID

        if (activityType == ActivityType.USER_LOGGED_IN) {
            result = joinPoint.proceed();
            targetId = extractTargetIdFromResult(joinPoint, result);

            if (targetId != null) {
                userId = targetId;
            }

            message = activityType.getType();

        } else if (activityType == ActivityType.TASK_STATUS_CHANGED) { // 작업 상태 변경 (ex: TO_DO -> IN_PROGRESS)
            Long taskId = null;
            UpdateStatusRequestDto req = null;

            // 메서드 인자에서 taskId와 요청 DTO 추출
            for (Object arg : args) {
                if (arg instanceof Long) {
                    taskId = (Long) arg;
                } else if (arg instanceof UpdateStatusRequestDto) {
                    req = (UpdateStatusRequestDto) arg;
                }
            }

            // 필수 인자가 없으면 로깅 없이 그대로 진행
            if (taskId == null || req == null) {
                return joinPoint.proceed();
            }

            // 현재 상태 조회, 메서드 실행, 바뀐 상태 확인
            TaskStatus fromStatus = activityLogService.findTaskStatus(taskId);
            result = joinPoint.proceed();
            TaskStatus toStatus = req.getStatus();

            if (fromStatus == toStatus) {
                return result; // 동일 상태면 로깅 안함
            }

            // 로그 메시지 생성 및 대상 ID 추출
            message = contentMessage(activityType, fromStatus, toStatus);
            targetId = extractTargetIdFromResult(joinPoint, result);
            userId = getCurrentUserId();

        } else {
            // 일반 로깅 처리 (생성, 삭제, 할당 등)
            result = joinPoint.proceed();
            targetId = extractTargetIdFromResult(joinPoint, result);
            message = activityType.getType(); // enum의 설명 텍스트 등
            userId = getCurrentUserId();
        }

        // 공통 로깅 처리
        activityLogService.saveActivityLog(userId, ip, method, url, activityType, targetId, message);
        log.info("ActivityLog: {}", message);

        return result;
    }

    private Long getCurrentUserId() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return null;
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Long id) {
            return id;
        }
        return null;
    }

    /**
     * 현재 요청을 가져오는 메서드
     */
    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return (attrs != null) ? attrs.getRequest() : null;
    }

    /**
     * 상태 변경 로그 메시지 생성
     */
    private String contentMessage(ActivityType type, TaskStatus from, TaskStatus to) {
        return type.format(from.getStatus(), to.getStatus());
    }

    /**
     * 메서드 실행 결과나 인자에서 타겟 ID를 추출
     * - 응답이 ResponseEntity<ApiResponse<T>> 형태이면 내부에서 ID 추출
     * - 그렇지 않으면 메서드 인자(Long) 중에서 사용
     */
    private Long extractTargetIdFromResult(JoinPoint joinPoint, Object result) {
        // 응답(ResponseEntity<ApiResponse<T>> 형태)에서 ID 추출
        if (result instanceof ResponseEntity<?> response) {
            Object body = response.getBody();
            if (body instanceof ApiResponse<?> apiResponse) {
                Object data = apiResponse.getData();

                if (data instanceof AuthResponseDto authResponse) {
                    if (authResponse.getUser() != null) {
                        Long id = authResponse.getUser().getId();
                        return id;
                    }
                }

                if (data instanceof Identifiable identifiable) {
                    return identifiable.getId();
                }
            }
        }

        // 실패 시, 메서드 인자에서 Long(ID) 추출
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof Long id) {
                return id; // @PathVariable Long taskId
            }
        }
        return null;
    }

}


