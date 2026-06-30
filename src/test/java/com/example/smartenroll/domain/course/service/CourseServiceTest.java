package com.example.smartenroll.domain.course.service;

import com.example.smartenroll.common.exception.CustomException;
import com.example.smartenroll.common.exception.ErrorCode;
import com.example.smartenroll.domain.course.dto.response.CourseCreateResponse;
import com.example.smartenroll.domain.course.dto.response.CourseDetailResponse;
import com.example.smartenroll.domain.course.dto.response.CourseListResponse;
import com.example.smartenroll.domain.course.entity.Course;
import com.example.smartenroll.domain.course.repository.CourseRepository;
import com.example.smartenroll.domain.lectureTime.dto.request.CourseCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseRepository courseRepository;

    @Test
    @DisplayName("강의를 생성한다.")
    void createCourse() {

        // given
        CourseCreateRequest request = CourseCreateRequest.builder()
                .courseCode("CS101")
                .title("웹프로그래밍")
                .capacity(30)
                .lectureTimes(List.of())
                .build();

        given(courseRepository.existsByCourseCode("CS101"))
                .willReturn(false);

        Course savedCourse = Course.builder()
                .courseCode("CS101")
                .title("웹프로그래밍")
                .capacity(30)
                .currentCount(0)
                .build();

        given(courseRepository.save(any(Course.class)))
                .willReturn(savedCourse);

        // when
        CourseCreateResponse response = courseService.create(request);

        // then
        assertThat(response.getCourseCode()).isEqualTo("CS101");
        assertThat(response.getTitle()).isEqualTo("웹프로그래밍");

        then(courseRepository).should().save(any(Course.class));
    }

    @Test
    @DisplayName("이미 존재하는 강의코드로 생성하면 예외가 발생한다.")
    void createCourseFail() {

        // given
        CourseCreateRequest request = CourseCreateRequest.builder()
                .courseCode("CS101")
                .title("웹프로그래밍")
                .capacity(30)
                .lectureTimes(List.of())
                .build();

        given(courseRepository.existsByCourseCode("CS101"))
                .willReturn(true);

        // when & then
        assertThatThrownBy(() -> courseService.create(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.COURSE_ALREADY_EXISTS.getMessage());

        then(courseRepository).should(never()).save(any(Course.class));
    }

    @Test
    @DisplayName("강의 목록을 조회한다.")
    void getCourses() {

        // given
        List<Course> courses = List.of(
                Course.builder()
                        .courseCode("CS101")
                        .title("웹프로그래밍")
                        .capacity(30)
                        .currentCount(0)
                        .build()
        );

        given(courseRepository.findAll())
                .willReturn(courses);

        // when
        List<CourseListResponse> result = courseService.getCourses();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCourseCode()).isEqualTo("CS101");
    }

    @Test
    @DisplayName("강의를 상세 조회한다.")
    void getCourse() {

        // given
        Course course = Course.builder()
                .courseCode("CS101")
                .title("웹프로그래밍")
                .capacity(30)
                .currentCount(0)
                .build();

        given(courseRepository.findById(1L))
                .willReturn(Optional.of(course));

        // when
        CourseDetailResponse response = courseService.getCourse(1L);

        // then
        assertThat(response.getCourseCode()).isEqualTo("CS101");
    }

    @Test
    @DisplayName("존재하지 않는 강의를 조회하면 예외가 발생한다.")
    void getCourseFail() {

        // given
        given(courseRepository.findById(1L))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->
                courseService.getCourse(1L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.COURSE_NOT_FOUND.getMessage());
    }
}