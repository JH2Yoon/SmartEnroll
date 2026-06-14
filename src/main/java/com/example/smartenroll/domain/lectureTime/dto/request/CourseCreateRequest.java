package com.example.smartenroll.domain.lectureTime.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class CourseCreateRequest {
    private String courseCode;
    private String title;
    private Integer capacity;

    private List<LectureTimeRequest> lectureTimes;
}
