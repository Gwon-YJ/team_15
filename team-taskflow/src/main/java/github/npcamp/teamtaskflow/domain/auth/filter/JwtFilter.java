package github.npcamp.teamtaskflow.domain.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import github.npcamp.teamtaskflow.domain.auth.utils.JwtUtil;
import github.npcamp.teamtaskflow.global.exception.ErrorCode;
import github.npcamp.teamtaskflow.global.payload.ApiResponse;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter implements Filter {

    private static final List<String> WHITE_LIST = List.of("/api/auth/register", "/api/auth/login");
    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();

        // 로그인, 회원가입 제외
        if (WHITE_LIST.contains(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        // 1. 요청 헤더에서 JWT 토큰 추출
        String token = httpRequest.getHeader("Authorization");

        // 2. 로그인하지 않은 사용자 (토큰이 없거나 형식이 잘못된 경우)
        if (token == null || !token.startsWith(JwtUtil.BEARER_PREFIX)) {
            jwtExceptionHandler(httpResponse, ErrorCode.LOGIN_REQUIRED);
            return;
        }

        // 3. Bearer 접두어 제거
        token = token.substring(JwtUtil.BEARER_PREFIX.length());

        // 4. 토큰 유효성 검사 실패
        if (!jwtUtil.validateToken(token)) {
            jwtExceptionHandler(httpResponse, ErrorCode.NOT_VALID_TOKEN);
            return;
        }

        // 토큰에서 UserId 추출
        Long userId = jwtUtil.extractUserId(token);

        // SecurityContextHolder에 user 저장
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userId, null, null));


        // 6. 다음 필터로 요청 전달
        chain.doFilter(httpRequest, httpResponse);
    }

    // 토큰 유효하지 않을 때 클라이언트에 JSON, ApiResponse로 응답하기 위한 예외처리 메서드
    public void jwtExceptionHandler(HttpServletResponse response, ErrorCode errorCode) {
        response.setStatus(errorCode.getStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = new ObjectMapper().writeValueAsString(ApiResponse.failure(errorCode.getMsg()));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
