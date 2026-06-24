package com.example.smartenroll.domain.student.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class StudentCreateRequest {

    @Schema(description = "학번")
    private Long studentNo;

    @Schema(description = "이름")
    private String name;
}