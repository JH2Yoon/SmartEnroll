package com.example.smartenroll.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // StudentMaster
    STUDENT_NOT_FOUND(HttpStatus.NOT_FOUND, "S001", "존재하지 않는 학번입니다."),

    // Member
    MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "M001", "이미 가입된 사용자입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M002", "존재하지 않는 사용자입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "M003", "비밀번호가 일치하지 않습니다."),

    // Auth
    INVALID_ADMIN_KEY(HttpStatus.FORBIDDEN, "A001", "관리자 키가 유효하지 않습니다."),

    // TOKEN
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "A002", "토큰이 만료되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A003", "유효하지 않은 토큰입니다."),
    STUDENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "STUDENT_409", "이미 등록된 학번입니다."),

    // COURSE
    COURSE_ALREADY_EXISTS(HttpStatus.CONFLICT, "C001", "이미 존재하는 강의입니다."),
    COURSE_NOT_FOUND(HttpStatus.NOT_FOUND, "C002", "존재하지 않는 강의입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}