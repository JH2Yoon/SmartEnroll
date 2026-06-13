package com.example.smartenroll.domain.member.dto.request;

import lombok.Getter;

@Getter
public class SignupRequest {
    private Long studentNo;
    private String password;
}