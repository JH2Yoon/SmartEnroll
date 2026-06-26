package com.example.smartenroll.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class SignupRequest {

    @Schema(description = "학번")
    private Long studentNo;

    @Schema(description = "비밀번호")
    private String password;
}