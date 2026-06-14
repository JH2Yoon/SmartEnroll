package com.example.smartenroll.domain.course.controller;

import com.example.smartenroll.domain.course.dto.response.CourseCreateResponse;
import com.example.smartenroll.domain.course.dto.response.CourseDetailResponse;
import com.example.smartenroll.domain.course.dto.response.CourseListResponse;
import com.example.smartenroll.domain.course.service.CourseService;
import com.example.smartenroll.domain.lectureTime.dto.request.CourseCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    // 강의 생성
    @PostMapping
    public ResponseEntity<CourseCreateResponse> create(@RequestBody CourseCreateRequest request) {
        CourseCreateResponse response = courseService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 강의 목록 조회
    @GetMapping
    public ResponseEntity<List<CourseListResponse>> getCourses() {

        return ResponseEntity.ok(courseService.getCourses());
    }

    // 강의 상세 조회
    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDetailResponse> getCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getCourse(courseId));
    }
}
