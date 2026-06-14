package com.example.smartenroll.domain.course.dto.response;

import com.example.smartenroll.domain.course.entity.Course;
import com.example.smartenroll.domain.lectureTime.dto.response.LectureTimeResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CourseCreateResponse {

    private String courseCode;
    private String title;
    private Integer capacity;

    private List<LectureTimeResponse> lectureTimes;

    public static CourseCreateResponse from(Course course) {

        List<LectureTimeResponse> lectureTimes =
                course.getLectureTimes().stream()
                        .map(LectureTimeResponse::from)
                        .toList();

        return CourseCreateResponse.builder()
                .courseCode(course.getCourseCode())
                .title(course.getTitle())
                .capacity(course.getCapacity())
                .lectureTimes(lectureTimes)
                .build();
    }
}