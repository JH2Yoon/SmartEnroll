package com.example.smartenroll.domain.lectureTime.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
public class LectureTimeRequest {

    @Schema(description = "요일")
    private DayOfWeek dayOfWeek;

    @Schema(description = "시작 시간")
    private LocalTime startTime;

    @Schema(description = "종료 시간")
    private LocalTime endTime;
}