package com.example.smartenroll.domain.member.controller;


import com.example.smartenroll.domain.member.dto.request.LoginRequest;
import com.example.smartenroll.domain.member.dto.request.RefreshRequest;
import com.example.smartenroll.domain.member.dto.request.SignupRequest;
import com.example.smartenroll.domain.member.dto.response.LoginResponse;
import com.example.smartenroll.domain.member.dto.response.SignupResponse;
import com.example.smartenroll.domain.member.dto.response.TokenResponse;
import com.example.smartenroll.domain.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc(addFilters = false)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MemberService memberService;

    @Test
    @DisplayName("회원가입 성공")
    void signup() throws Exception {

        // given
        SignupRequest request = SignupRequest.builder()
                .studentNo(20240001L)
                .password("1234")
                .build();

        SignupResponse response = SignupResponse.builder()
                .memberUsername("홍길동")
                .studentNo(20240001L)
                .build();

        given(memberService.signup(any(SignupRequest.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(post("/v1/members/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.memberUsername").value("홍길동"))
                .andExpect(jsonPath("$.studentNo").value(20240001));
    }

    @Test
    @DisplayName("로그인 성공")
    void login() throws Exception {

        // given
        LoginRequest request = LoginRequest.builder()
                .username("20240001")
                .password("1234")
                .build();

        LoginResponse response = LoginResponse.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .build();

        given(memberService.login(any(LoginRequest.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(post("/v1/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }

    @Test
    @DisplayName("토큰 재발급 성공")
    void refresh() throws Exception {

        // given
        RefreshRequest request = RefreshRequest.builder()
                .refreshToken("refresh-token")
                .build();

        TokenResponse response = TokenResponse.builder()
                .accessToken("new-access-token")
                .build();

        given(memberService.refresh(any(RefreshRequest.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(post("/v1/members/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new-access-token"));
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logout() throws Exception {

        // given
        Authentication authentication = mock(Authentication.class);

        given(authentication.getPrincipal()).willReturn(1L);

        // when & then
        mockMvc.perform(post("/v1/members/logout")
                        .principal(authentication))
                .andExpect(status().isOk());

        then(memberService).should().logout(1L);
    }
}