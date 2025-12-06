package com.example.job.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter                             // message 필드에 대한 getter 생성
@AllArgsConstructor                // message 하나를 받는 생성자 자동 생성
public class MessageResponse {

    // API 성공/실패 여부 등을 간단히 전달할 때 사용하는 메시지
    private String message;
}
