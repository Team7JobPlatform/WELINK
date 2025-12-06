package com.example.job.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration  // 스프링 설정 클래스임을 표시
public class CorsConfig {

    @Bean  // WebMvcConfigurer 빈을 등록해서 전역 CORS 설정에 사용
    public WebMvcConfigurer corsConfigurer() {
        // 익명 클래스로 WebMvcConfigurer 구현
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // 서버의 모든 API 경로에 대해 CORS 설정 적용
                registry.addMapping("/**")
                        // 로컬 프론트엔드(vite, React 등)가 띄워지는 주소만 허용
                        .allowedOrigins("http://localhost:5173")
                        // 허용할 HTTP 메서드 목록
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        // 요청 헤더는 어떤 것이든 허용
                        .allowedHeaders("*")
                        // 쿠키·인증정보(Authorization 헤더 등) 포함한 요청 허용
                        .allowCredentials(true);
            }
        };
    }
}
