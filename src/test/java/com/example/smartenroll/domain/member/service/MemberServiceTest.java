package com.example.smartenroll.domain.member.service;

import com.example.smartenroll.common.exception.CustomException;
import com.example.smartenroll.common.exception.ErrorCode;
import com.example.smartenroll.common.jwt.JwtUtil;
import com.example.smartenroll.domain.course.dto.response.CourseCreateResponse;
import com.example.smartenroll.domain.course.dto.response.CourseDetailResponse;
import com.example.smartenroll.domain.course.dto.response.CourseListResponse;
import com.example.smartenroll.domain.course.entity.Course;
import com.example.smartenroll.domain.course.repository.CourseRepository;
import com.example.smartenroll.domain.lectureTime.dto.request.CourseCreateRequest;
import com.example.smartenroll.domain.member.dto.request.LoginRequest;
import com.example.smartenroll.domain.member.dto.request.RefreshRequest;
import com.example.smartenroll.domain.member.dto.request.SignupRequest;
import com.example.smartenroll.domain.member.dto.response.LoginResponse;
import com.example.smartenroll.domain.member.dto.response.SignupResponse;
import com.example.smartenroll.domain.member.dto.response.TokenResponse;
import com.example.smartenroll.domain.member.entity.Member;
import com.example.smartenroll.domain.member.entity.MemberRoleEnum;
import com.example.smartenroll.domain.member.repository.MemberRepository;
import com.example.smartenroll.domain.student.entity.StudentMaster;
import com.example.smartenroll.domain.student.repository.StudentMasterRepository;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private StudentMasterRepository studentMasterRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;


    @Test
    @DisplayName("회원가입 성공")
    void signup() {

        // given
        SignupRequest request = SignupRequest.builder()
                .studentNo(20240001L)
                .password("1234")
                .build();

        StudentMaster student = StudentMaster.builder()
                .studentNo(20240001L)
                .name("홍길동")
                .build();

        given(studentMasterRepository.findByStudentNo(20240001L))
                .willReturn(Optional.of(student));

        given(memberRepository.existsByUsername("20240001"))
                .willReturn(false);

        given(passwordEncoder.encode("1234"))
                .willReturn("encodedPassword");

        // when
        SignupResponse response = memberService.signup(request);

        // then
        assertThat(response.getMemberUsername()).isEqualTo("홍길동");
        assertThat(response.getStudentNo()).isEqualTo(20240001L);

        then(memberRepository).should().save(any(Member.class));
    }

    @Test
    @DisplayName("학생 정보가 없으면 예외가 발생한다.")
    void signupFailStudentNotFound() {

        // given
        SignupRequest request = SignupRequest.builder()
                .studentNo(20240001L)
                .password("1234")
                .build();

        given(studentMasterRepository.findByStudentNo(20240001L))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberService.signup(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.STUDENT_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("이미 가입된 회원이면 예외가 발생한다.")
    void signupFailMemberExists() {

        // given
        SignupRequest request = SignupRequest.builder()
                .studentNo(20240001L)
                .password("1234")
                .build();

        StudentMaster student = StudentMaster.builder()
                .studentNo(20240001L)
                .name("홍길동")
                .build();

        given(studentMasterRepository.findByStudentNo(20240001L))
                .willReturn(Optional.of(student));

        given(memberRepository.existsByUsername("20240001"))
                .willReturn(true);

        // when & then
        assertThatThrownBy(() -> memberService.signup(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.MEMBER_ALREADY_EXISTS.getMessage());
    }

    @Test
    @DisplayName("로그인 성공")
    void login() {

        // given
        LoginRequest request = LoginRequest.builder()
                .username("20240001")
                .password("1234")
                .build();

        Member member = Member.builder()
                .id(1L)
                .username("20240001")
                .password("encodedPassword")
                .role(MemberRoleEnum.STUDENT)
                .build();

        given(memberRepository.findByUsername("20240001"))
                .willReturn(Optional.of(member));

        given(passwordEncoder.matches("1234", "encodedPassword"))
                .willReturn(true);

        given(jwtUtil.createAccessToken(1L, MemberRoleEnum.STUDENT))
                .willReturn("accessToken");

        given(jwtUtil.createRefreshToken(1L))
                .willReturn("refreshToken");

        given(redisTemplate.opsForValue())
                .willReturn(valueOperations);

        // when
        LoginResponse response = memberService.login(request);

        // then
        assertThat(response.getAccessToken()).isEqualTo("accessToken");
        assertThat(response.getRefreshToken()).isEqualTo("refreshToken");
        assertThat(response.getRole()).isEqualTo(MemberRoleEnum.STUDENT);

        then(valueOperations).should().set(
                "RT:1",
                "refreshToken",
                7,
                TimeUnit.DAYS
        );
    }

    @Test
    @DisplayName("존재하지 않는 회원은 로그인할 수 없다.")
    void loginFailMemberNotFound() {

        // given
        LoginRequest request = LoginRequest.builder()
                .username("20240001")
                .password("1234")
                .build();

        given(memberRepository.findByUsername("20240001"))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberService.login(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 로그인할 수 없다.")
    void loginFailInvalidPassword() {

        // given
        LoginRequest request = LoginRequest.builder()
                .username("20240001")
                .password("wrongPassword")
                .build();

        Member member = Member.builder()
                .id(1L)
                .username("20240001")
                .password("encodedPassword")
                .role(MemberRoleEnum.STUDENT)
                .build();

        given(memberRepository.findByUsername("20240001"))
                .willReturn(Optional.of(member));

        given(passwordEncoder.matches("wrongPassword", "encodedPassword"))
                .willReturn(false);

        // when & then
        assertThatThrownBy(() -> memberService.login(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_PASSWORD.getMessage());
    }

    @Test
    @DisplayName("리프레시 토큰으로 Access Token을 재발급한다.")
    void refresh() {

        // given
        RefreshRequest request = RefreshRequest.builder()
                .refreshToken("refreshToken")
                .build();

        Claims claims = mock(Claims.class);

        Member member = Member.builder()
                .id(1L)
                .username("20240001")
                .role(MemberRoleEnum.STUDENT)
                .build();

        given(jwtUtil.parseClaims("refreshToken"))
                .willReturn(claims);

        given(claims.getSubject())
                .willReturn("1");

        given(redisTemplate.opsForValue())
                .willReturn(valueOperations);

        given(valueOperations.get("RT:1"))
                .willReturn("refreshToken");

        given(memberRepository.findById(1L))
                .willReturn(Optional.of(member));

        given(jwtUtil.createAccessToken(1L, MemberRoleEnum.STUDENT))
                .willReturn("newAccessToken");

        // when
        TokenResponse response = memberService.refresh(request);

        // then
        assertThat(response.getAccessToken()).isEqualTo("newAccessToken");
    }

    @Test
    @DisplayName("Refresh Token이 일치하지 않으면 예외가 발생한다.")
    void refreshFail() {

        // given
        RefreshRequest request = RefreshRequest.builder()
                .refreshToken("refreshToken")
                .build();

        Claims claims = mock(Claims.class);

        given(jwtUtil.parseClaims("refreshToken"))
                .willReturn(claims);

        given(claims.getSubject())
                .willReturn("1");

        given(redisTemplate.opsForValue())
                .willReturn(valueOperations);

        given(valueOperations.get("RT:1"))
                .willReturn("anotherToken");

        // when & then
        assertThatThrownBy(() -> memberService.refresh(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_TOKEN.getMessage());
    }

    @Test
    @DisplayName("로그아웃 시 Refresh Token을 삭제한다.")
    void logout() {

        // given
        Long memberId = 1L;

        // when
        memberService.logout(memberId);

        // then
        then(redisTemplate).should()
                .delete("RT:1");
    }
}