package com.example.smartenroll.domain.lectureTime.dto.request;

import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
public class LectureTimeRequest {

    private DayOfWeek dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;
}