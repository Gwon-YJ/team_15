package github.npcamp.teamtaskflow.global.config;


import github.npcamp.teamtaskflow.domain.auth.service.LogOutService;
import github.npcamp.teamtaskflow.global.filter.JwtFilter;
import github.npcamp.teamtaskflow.global.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final JwtUtil jwtUtil;
    private final LogOutService logOutService;


    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter() {
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtFilter(jwtUtil, logOutService));  // 주입
        registrationBean.setOrder(1);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
