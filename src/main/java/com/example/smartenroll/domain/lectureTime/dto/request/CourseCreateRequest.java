package com.example.smartenroll.domain.lectureTime.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CourseCreateRequest {

    @Schema(description = "강의코드")
    private String courseCode;

    @Schema(description = "강의 제목")
    private String title;

    @Schema(description = "현재 수강신청한 인원")
    private Integer capacity;

    @Schema(description = "강의 시간")
    private List<LectureTimeRequest> lectureTimes;
}
