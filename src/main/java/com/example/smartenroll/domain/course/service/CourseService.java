package com.example.smartenroll.domain.course.service;

import com.example.smartenroll.common.exception.CustomException;
import com.example.smartenroll.common.exception.ErrorCode;
import com.example.smartenroll.domain.course.dto.response.CourseCreateResponse;
import com.example.smartenroll.domain.course.dto.response.CourseDetailResponse;
import com.example.smartenroll.domain.course.dto.response.CourseListResponse;
import com.example.smartenroll.domain.course.entity.Course;
import com.example.smartenroll.domain.course.repository.CourseRepository;
import com.example.smartenroll.domain.lectureTime.dto.request.CourseCreateRequest;
import com.example.smartenroll.domain.lectureTime.dto.request.LectureTimeRequest;
import com.example.smartenroll.domain.lectureTime.dto.response.LectureTimeResponse;
import com.example.smartenroll.domain.lectureTime.entity.LectureTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository courseRepository;

    @Transactional
    public CourseCreateResponse create(CourseCreateRequest request) {
        if (courseRepository.existsByCourseCode(request.getCourseCode())) {
            throw new CustomException(ErrorCode.COURSE_ALREADY_EXISTS);
        }

        Course course = Course.builder()
                .courseCode(request.getCourseCode())
                .title(request.getTitle())
                .capacity(request.getCapacity())
                .currentCount(0)
                .build();

        for (LectureTimeRequest lectureRequest : request.getLectureTimes()) {

            LectureTime lectureTime = LectureTime.builder()
                    .course(course)
                    .dayOfWeek(lectureRequest.getDayOfWeek())
                    .startTime(lectureRequest.getStartTime())
                    .endTime(lectureRequest.getEndTime())
                    .build();

            course.getLectureTimes().add(lectureTime);
        }

        Course savedCourse = courseRepository.save(course);

        return CourseCreateResponse.from(savedCourse);
    }

    public List<CourseListResponse> getCourses() {
        return courseRepository.findAll()
                .stream()
                .map(CourseListResponse::from)
                .toList();
    }


    public CourseDetailResponse getCourse(Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new CustomException(ErrorCode.COURSE_NOT_FOUND));

        return CourseDetailResponse.from(course);
    }
}
