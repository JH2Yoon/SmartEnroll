package com.example.smartenroll.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class SignupResponse {

    @Schema(description = "이름")
    private String memberUsername;

    @Schema(description = "학번")
    private Long studentNo;

    public SignupResponse(String memberUsername, Long studentNo) {
        this.memberUsername = memberUsername;
        this.studentNo = studentNo;
    }
}