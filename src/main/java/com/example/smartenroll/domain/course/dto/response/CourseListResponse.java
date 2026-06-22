package com.example.smartenroll.domain.course.dto.response;

import com.example.smartenroll.domain.course.entity.Course;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CourseListResponse {

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