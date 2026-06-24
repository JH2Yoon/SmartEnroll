package com.example.smartenroll.domain.course.controller;

import com.example.smartenroll.domain.course.dto.response.CourseCreateResponse;
import com.example.smartenroll.domain.course.dto.response.CourseDetailResponse;
import com.example.smartenroll.domain.course.dto.response.CourseListResponse;
import com.example.smartenroll.domain.course.service.CourseService;
import com.example.smartenroll.domain.lectureTime.dto.request.CourseCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "강의", description = "강의 API")
@RestController
@RequestMapping("/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "강의 생성",
            description = "강의를 생성합니다."
    )
    @PostMapping
    public ResponseEntity<CourseCreateResponse> create(@RequestBody CourseCreateRequest request) {
        CourseCreateResponse response = courseService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "강의 목록 조회",
            description = "강의 목록을 조회합니다."
    )
    @GetMapping
    public ResponseEntity<List<CourseListResponse>> getCourses() {

        return ResponseEntity.ok(courseService.getCourses());
    }

    @Operation(summary = "강의 상세 조회",
            description = "강의를 상세 조회합니다."
    )
    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDetailResponse> getCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getCourse(courseId));
    }
}