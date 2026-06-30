package com.example.smartenroll.domain.member.dto.response;

import com.example.smartenroll.domain.member.entity.MemberRoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponse {

    @Schema(description = "액세스 토큰")
    private String accessToken;

    @Schema(description = "리프레시 토큰")
    private String refreshToken;

    @Schema(description = "권한 (STUDENT / ADMIN)")
    private MemberRoleEnum role;
}