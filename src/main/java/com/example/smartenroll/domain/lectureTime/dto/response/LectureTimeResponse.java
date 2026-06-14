package com.example.smartenroll.domain.lectureTime.dto.response;

import com.example.smartenroll.domain.lectureTime.entity.LectureTime;
import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Builder
public class LectureTimeResponse {

    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public static LectureTimeResponse from(LectureTime lectureTime) {
        return LectureTimeResponse.builder()
                .dayOfWeek(lectureTime.getDayOfWeek())
                .startTime(lectureTime.getStartTime())
                .endTime(lectureTime.getEndTime())
                .build();
    }
}