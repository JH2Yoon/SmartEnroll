package com.example.smartenroll.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshRequest {

    @Schema(description = "리프레시 토큰")
    @NotBlank(message = "리프레시 토큰은 필수입니다.")
    private String refreshToken;
}