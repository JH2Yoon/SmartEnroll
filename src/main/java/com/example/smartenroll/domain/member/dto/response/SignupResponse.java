package com.example.smartenroll.domain.member.dto.response;

import lombok.Getter;

@Getter
public class SignupResponse {
    private String memberUsername;
    private Long studentNo;

    public SignupResponse(String memberUsername, Long studentNo) {
        this.memberUsername = memberUsername;
        this.studentNo = studentNo;
    }
}