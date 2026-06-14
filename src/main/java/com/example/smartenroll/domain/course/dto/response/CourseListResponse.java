package com.example.smartenroll.domain.course.dto.response;

import com.example.smartenroll.domain.course.entity.Course;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CourseListResponse {

    private Long id;
    private String courseCode;
    private String title;
    private Integer capacity;
    private Integer currentCount;

    public static CourseListResponse from(Course course) {
        return CourseListResponse.builder()
                .id(course.getId())
                .courseCode(course.getCourseCode())
                .title(course.getTitle())
                .capacity(course.getCapacity())
                .currentCount(course.getCurrentCount())
                .build();
    }
}