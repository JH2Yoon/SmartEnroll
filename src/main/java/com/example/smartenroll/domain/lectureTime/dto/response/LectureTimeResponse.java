package com.example.smartenroll.domain.lectureTime.dto.response;

import com.example.smartenroll.domain.lectureTime.entity.LectureTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Builder
public class LectureTimeResponse {

    @Schema(description = "요일")
    private DayOfWeek dayOfWeek;

    @Schema(description = "시작 시간")
    private LocalTime startTime;

    @Schema(description = "종료 시간")
    private LocalTime endTime;

    public static LectureTimeResponse from(LectureTime lectureTime) {
        return LectureTimeResponse.builder()
                .dayOfWeek(lectureTime.getDayOfWeek())
                .startTime(lectureTime.getStartTime())
                .endTime(lectureTime.getEndTime())
                .build();
    }
}