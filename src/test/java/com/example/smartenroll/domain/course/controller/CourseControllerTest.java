package com.example.smartenroll.domain.course.controller;

import com.example.smartenroll.common.jwt.JwtUtil;
import com.example.smartenroll.domain.course.dto.response.CourseCreateResponse;
import com.example.smartenroll.domain.course.dto.response.CourseDetailResponse;
import com.example.smartenroll.domain.course.dto.response.CourseListResponse;
import com.example.smartenroll.domain.course.service.CourseService;
import com.example.smartenroll.domain.lectureTime.dto.request.CourseCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
@AutoConfigureMockMvc(addFilters = false)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CourseService courseService;

    @Test
    @DisplayName("강의 생성 성공")
    void createCourse() throws Exception {

        // given
        String request = """
                {
                  "courseCode": "CS101",
                  "title": "웹프로그래밍",
                  "capacity": 30,
                  "lectureTimes": []
                }
                """;

        CourseCreateResponse response = CourseCreateResponse.builder()
                .courseCode("CS101")
                .title("웹프로그래밍")
                .capacity(30)
                .lectureTimes(List.of())
                .build();

        given(courseService.create(any(CourseCreateRequest.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(post("/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.courseCode").value("CS101"))
                .andExpect(jsonPath("$.title").value("웹프로그래밍"))
                .andExpect(jsonPath("$.capacity").value(30));
    }

    @Test
    @DisplayName("강의 목록 조회 성공")
    void getCourses() throws Exception {

        // given
        List<CourseListResponse> responses = List.of(
                CourseListResponse.builder()
                        .courseCode("CS101")
                        .title("웹프로그래밍")
                        .capacity(30)
                        .currentCount(0)
                        .build()
        );

        given(courseService.getCourses())
                .willReturn(responses);

        // when & then
        mockMvc.perform(get("/v1/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].courseCode").value("CS101"))
                .andExpect(jsonPath("$[0].title").value("웹프로그래밍"))
                .andExpect(jsonPath("$[0].capacity").value(30))
                .andExpect(jsonPath("$[0].currentCount").value(0));
    }

    @Test
    @DisplayName("강의 상세 조회 성공")
    void getCourse() throws Exception {

        // given
        CourseDetailResponse response = CourseDetailResponse.builder()
                .courseCode("CS101")
                .title("웹프로그래밍")
                .capacity(30)
                .currentCount(0)
                .lectureTimes(List.of())
                .build();

        given(courseService.getCourse(1L))
                .willReturn(response);

        // when & then
        mockMvc.perform(get("/v1/courses/{courseId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseCode").value("CS101"))
                .andExpect(jsonPath("$.title").value("웹프로그래밍"))
                .andExpect(jsonPath("$.capacity").value(30))
                .andExpect(jsonPath("$.currentCount").value(0))
                .andExpect(jsonPath("$.lectureTimes").isArray());
    }
}