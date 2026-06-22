package com.example.smartenroll.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class LoginRequest {

    @Schema(description = "학번")
    private String username;

    @Schema(description = "비밀번호")
    private String password;
}