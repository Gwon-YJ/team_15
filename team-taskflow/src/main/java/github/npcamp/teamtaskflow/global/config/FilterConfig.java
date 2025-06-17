//package github.npcamp.teamtaskflow.global.config;
//
//import github.npcamp.teamtaskflow.domain.auth.filter.JwtFilter;
//import github.npcamp.teamtaskflow.domain.auth.utils.JwtUtil;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
////@Configuration
//public class FilterConfig {
//
//    @Bean
//    public FilterRegistrationBean<JwtFilter> jwtFilter(JwtUtil jwtUtil) {
//        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
//
//        registrationBean.setFilter(new JwtFilter(jwtUtil));
//        registrationBean.setOrder(1);
//        registrationBean.addUrlPatterns("/*");
//
//        return registrationBean;
//    }
//}
