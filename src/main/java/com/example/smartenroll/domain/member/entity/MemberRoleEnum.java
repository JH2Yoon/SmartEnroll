package com.example.smartenroll.domain.member.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 역할", example = "ADMIN")
public enum MemberRoleEnum {
    STUDENT,
    ADMIN
}