package com.example.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.util.Properties; // 추가됨

@SpringBootApplication
public class Team7JOBApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Team7JOBApplication.class);

        // ★ 핵심: 설정 파일을 못 읽는 상황을 대비해, 코드로 강제 설정합니다.
        Properties defaults = new Properties();
        defaults.setProperty("spring.jpa.database-platform", "org.hibernate.dialect.MariaDBDialect"); // 방언 강제 고정
        defaults.setProperty("spring.jpa.hibernate.ddl-auto", "update"); // 테이블 자동 생성
        app.setDefaultProperties(defaults);

        app.run(args);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        // 드라이버 & 주소
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/jobDB?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8");

        // 아이디
        dataSource.setUsername("root");

        // ★★★ [중요] 본인 비밀번호로 꼭 바꾸세요! ★★★
        dataSource.setPassword("7988");

        return dataSource;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}