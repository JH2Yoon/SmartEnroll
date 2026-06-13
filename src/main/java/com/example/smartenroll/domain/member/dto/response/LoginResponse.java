package com.example.smartenroll.domain.member.dto.response;

import com.example.smartenroll.domain.member.entity.MemberRoleEnum;
import lombok.*;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;
    private String refreshToken;

    private MemberRoleEnum role;
}