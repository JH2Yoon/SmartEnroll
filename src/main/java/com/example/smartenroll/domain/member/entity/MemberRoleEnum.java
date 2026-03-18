package com.example.smartenroll.domain.member.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 역할", example = "ADMIN", allowableValues = {"STUDENT", "ADMIN"})
public enum MemberRoleEnum {
    STUDENT(Authority.STUDENT),
    ADMIN(Authority.ADMIN);

    private final String authority;

    MemberRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String STUDENT = "ROLE_STUDENT";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}