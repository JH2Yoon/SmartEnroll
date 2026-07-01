package com.example.smartenroll.domain.registration.controller;

import com.example.smartenroll.domain.registration.dto.response.RegistrationResponse;
import com.example.smartenroll.domain.registration.service.RegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegistrationController.class)
@AutoConfigureMockMvc(addFilters = false)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RegistrationService registrationService;

    @Test
    @DisplayName("수강신청 성공")
    void register() throws Exception {

        // given
        Authentication authentication = mock(Authentication.class);

        given(authentication.getPrincipal())
                .willReturn(1L);

        // when & then
        mockMvc.perform(post("/v1/registrations/{courseId}", 1L)
                        .principal(authentication))
                .andExpect(status().isOk());

        then(registrationService)
                .should()
                .register(1L, 1L);
    }

    @Test
    @DisplayName("수강취소 성공")
    void cancel() throws Exception {

        // given
        Authentication authentication = mock(Authentication.class);

        given(authentication.getPrincipal())
                .willReturn(1L);

        // when & then
        mockMvc.perform(delete("/v1/registrations/{registrationId}", 10L)
                        .principal(authentication))
                .andExpect(status().isNoContent());

        then(registrationService)
                .should()
                .cancel(1L, 10L);
    }

    @Test
    @DisplayName("내 수강목록 조회 성공")
    void getMyCourses() throws Exception {

        // given
        Authentication authentication = mock(Authentication.class);

        given(authentication.getPrincipal())
                .willReturn(1L);

        List<RegistrationResponse> responses = List.of(
                RegistrationResponse.builder()
                        .courseCode("CS101")
                        .title("웹프로그래밍")
                        .build()
        );

        given(registrationService.getMyCourses(1L))
                .willReturn(responses);

        // when & then
        mockMvc.perform(get("/v1/registrations/me")
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].courseCode").value("CS101"))
                .andExpect(jsonPath("$[0].title").value("웹프로그래밍"));
    }
}