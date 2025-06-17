package github.npcamp.teamtaskflow.global.filter;

import github.npcamp.teamtaskflow.domain.auth.service.AuthService;
import github.npcamp.teamtaskflow.global.utils.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j(topic = "JwtFilter")
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;
    private final AuthService logOutService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();
        String username = null;
        String jwt = null;

        // 교통카드가 있는지 없는지 검사
        // 유효한지 아닌지 검사

        String authorizationHeader = httpRequest.getHeader("Authorization");


        // 처음 로그인 하는 거야? 그럼 JWT 토큰이 없을 것이니 토큰 먼저 발급 받아!
        if (requestURI.equals("/api/auth/login") || requestURI.equals("/api/auth/register")) {
            chain.doFilter(request, response);
            return;
        }

        // 로그인 하는게 아니네? 그럼 JWT 토큰 있어?

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.info("JWT 토큰이 필요 합니다.");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.setContentType("application/json; charset=UTF-8");
            httpResponse.getWriter().write("JWT 토큰이 필요 합니다.");
            httpResponse.getWriter().flush();
            return;
        }

        // JWT 토큰이 있구만 그럼 JWT 토큰 유효해?
        // 1. Secret Key 내가 만든 거랑 동일해?
        // 2. JWT 시간 만료 된거 아니야?

        jwt = authorizationHeader.substring(7);

        // Secret Key 는 내가 만든게 맞는지 검증 만료 기간 지났는지 검증
        if (!jwtUtil.validateToken(jwt)) {
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.setContentType("application/json; charset=UTF-8");
            httpResponse.getWriter().write("{\"error\": \"Unauthorized\"}");
            return;
        }

        // 블랙리스트 확인 (로그아웃된 토큰인지)
        if (logOutService.isTokenBlacklisted(jwt)) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.setContentType("application/json; charset=UTF-8");
            httpResponse.getWriter().write("{\"error\": \"로그아웃된 토큰입니다. 다시 로그인 해주세요.\"}");
            httpResponse.getWriter().flush();
            return;
        }

        // 그럼 너가 가져온 JWT 토큰은 유효한 토큰이군! 통과!


        // 이제부터는 JWT 토큰에 어떤 정보가 들어가 있는지 살펴보자!

        // JWT 사용자의 이름을 확인 해보자
        username = jwtUtil.extractUsername(jwt);

        // 만약 요청한 API 가 관리자 전용 API 인 경우에
        if (requestURI.startsWith("/api/admin")) {

            // JWT에 관리자 권한이 있는지 확인
            if (jwtUtil.hasRole(jwt, "ADMIN")) {
                chain.doFilter(request, response);
            } else {
                // 권한이 없으면 403 Forbidden 응답
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                httpResponse.setCharacterEncoding("UTF-8");
                httpResponse.setContentType("application/json; charset=UTF-8");
                httpResponse.getWriter().write("접근 권한이 없습니다.");
                httpResponse.getWriter().flush();
                return;
            }
        }

        // 만약 요청한 API 가 사용자 전용 API 인 경우에
        if (requestURI.startsWith("/api/users")) {

            // JWT에 사용자 권한이 있는지 확인
            if (jwtUtil.hasRole(jwt, "USER")) {
                chain.doFilter(request, response);
            } else {
                // 권한이 없으면 403 Forbidden 응답
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "접근 권한이 없습니다.");
            }
            return;
        }

        // 전용 API가 아닌 일반 API의 경우
        chain.doFilter(request, response);

    }
}
