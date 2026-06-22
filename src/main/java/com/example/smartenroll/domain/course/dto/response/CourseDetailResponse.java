package com.example.smartenroll.domain.course.dto.response;

import com.example.smartenroll.domain.course.entity.Course;
import com.example.smartenroll.domain.lectureTime.dto.response.LectureTimeResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CourseDetailResponse {

    @Schema(description = "강의 고유 식별자", example = "1")
    private Long id;

    @Schema(description = "강의코드")
    private String courseCode;

    @Schema(description = "강의 제목")
    private String title;

    @Schema(description = "총 수강가능한 인원")
    private Integer capacity;

    @Schema(description = "현재 수강신청한 인원")
    private Integer currentCount;

    @Schema(description = "강의 시간")
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