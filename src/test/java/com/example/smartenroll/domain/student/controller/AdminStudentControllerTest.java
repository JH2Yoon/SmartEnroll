package com.example.smartenroll.domain.student.controller;

import com.example.smartenroll.domain.student.dto.request.StudentCreateRequest;
import com.example.smartenroll.domain.student.service.StudentMasterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminStudentController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminStudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StudentMasterService studentMasterService;

    @Test
    @DisplayName("학생 등록 성공")
    void create() throws Exception {

        // given
        StudentCreateRequest request = StudentCreateRequest.builder()
                .studentNo(20240001L)
                .name("홍길동")
                .build();

        // when & then
        mockMvc.perform(post("/v1/admin/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        then(studentMasterService)
                .should()
                .create(any(StudentCreateRequest.class));
    }
}