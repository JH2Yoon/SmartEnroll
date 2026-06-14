package com.example.smartenroll.domain.course.dto.response;

import com.example.smartenroll.domain.course.entity.Course;
import com.example.smartenroll.domain.lectureTime.dto.response.LectureTimeResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CourseDetailResponse {

    private Long id;
    private String courseCode;
    private String title;
    private Integer capacity;
    private Integer currentCount;

    private List<LectureTimeResponse> lectureTimes;

    public static CourseDetailResponse from(Course course) {

        List<LectureTimeResponse> lectureTimes =
                course.getLectureTimes().stream()
                        .map(LectureTimeResponse::from)
                        .toList();

        return CourseDetailResponse.builder()
                .id(course.getId())
                .courseCode(course.getCourseCode())
                .title(course.getTitle())
                .capacity(course.getCapacity())
                .currentCount(course.getCurrentCount())
                .lectureTimes(lectureTimes)
                .build();
    }
}