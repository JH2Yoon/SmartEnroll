package com.example.smartenroll.domain.registration.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegistrationResponse {

    @Schema(description = "수강신청 ID", example = "1")
    private Long registrationId;

    @Schema(description = "강의코드")
    private String courseCode;

    @Schema(description = "강의 제목")
    private String title;
}