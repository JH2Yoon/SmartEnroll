package com.example.smartenroll.domain.registration.service;

import com.example.smartenroll.common.exception.CustomException;
import com.example.smartenroll.common.exception.ErrorCode;
import com.example.smartenroll.domain.course.entity.Course;
import com.example.smartenroll.domain.course.repository.CourseRepository;
import com.example.smartenroll.domain.member.entity.Member;
import com.example.smartenroll.domain.member.repository.MemberRepository;
import com.example.smartenroll.domain.registration.dto.response.RegistrationResponse;
import com.example.smartenroll.domain.registration.entity.Registration;
import com.example.smartenroll.domain.registration.repository.RegistrationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @InjectMocks
    private RegistrationService registrationService;

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CourseRepository courseRepository;

    @Test
    @DisplayName("수강신청 성공")
    void register() {

        // given
        Member member = Member.builder()
                .id(1L)
                .build();

        Course course = Course.builder()
                .courseCode("CS101")
                .title("웹프로그래밍")
                .capacity(30)
                .currentCount(0)
                .build();

        given(memberRepository.findById(1L))
                .willReturn(Optional.of(member));

        given(courseRepository.findByIdWithLock(1L))
                .willReturn(Optional.of(course));

        given(registrationRepository.existsByMemberAndCourse(member, course))
                .willReturn(false);

        given(registrationRepository.findAllByMember(member))
                .willReturn(List.of());

        // when
        registrationService.register(1L, 1L);

        // then
        assertThat(course.getCurrentCount()).isEqualTo(1);

        then(registrationRepository)
                .should()
                .save(any(Registration.class));
    }

    @Test
    @DisplayName("존재하지 않는 회원이면 예외가 발생한다.")
    void registerMemberNotFound() {

        // given
        given(memberRepository.findById(1L))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->
                registrationService.register(1L, 1L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("존재하지 않는 강의면 예외가 발생한다.")
    void registerCourseNotFound() {

        // given
        Member member = Member.builder().id(1L).build();

        given(memberRepository.findById(1L))
                .willReturn(Optional.of(member));

        given(courseRepository.findByIdWithLock(1L))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->
                registrationService.register(1L, 1L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.COURSE_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("이미 신청한 강의면 예외가 발생한다.")
    void registerDuplicate() {

        // given
        Member member = Member.builder().id(1L).build();

        Course course = Course.builder()
                .courseCode("CS101")
                .capacity(30)
                .currentCount(0)
                .build();

        given(memberRepository.findById(1L))
                .willReturn(Optional.of(member));

        given(courseRepository.findByIdWithLock(1L))
                .willReturn(Optional.of(course));

        given(registrationRepository.existsByMemberAndCourse(member, course))
                .willReturn(true);

        // when & then
        assertThatThrownBy(() ->
                registrationService.register(1L, 1L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ALREADY_REGISTERED.getMessage());
    }

    @Test
    @DisplayName("정원이 가득 차면 신청할 수 없다.")
    void registerCourseFull() {

        // given
        Member member = Member.builder().id(1L).build();

        Course course = Course.builder()
                .capacity(30)
                .currentCount(30)
                .build();

        given(memberRepository.findById(1L))
                .willReturn(Optional.of(member));

        given(courseRepository.findByIdWithLock(1L))
                .willReturn(Optional.of(course));

        given(registrationRepository.existsByMemberAndCourse(member, course))
                .willReturn(false);

        // when & then
        assertThatThrownBy(() ->
                registrationService.register(1L, 1L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.COURSE_FULL.getMessage());
    }

    @Test
    @DisplayName("수강취소 성공")
    void cancel() {

        // given
        Member member = Member.builder()
                .id(1L)
                .build();

        Course course = Course.builder()
                .capacity(30)
                .currentCount(1)
                .build();

        Registration registration = Registration.builder()
                .member(member)
                .course(course)
                .build();

        given(memberRepository.findById(1L))
                .willReturn(Optional.of(member));

        given(registrationRepository.findById(1L))
                .willReturn(Optional.of(registration));

        // when
        registrationService.cancel(1L, 1L);

        // then
        assertThat(course.getCurrentCount()).isEqualTo(0);

        then(registrationRepository)
                .should()
                .delete(registration);
    }

    @Test
    @DisplayName("내 수강목록을 조회한다.")
    void getMyCourses() {

        // given
        Member member = Member.builder()
                .id(1L)
                .build();

        Course course = Course.builder()
                .courseCode("CS101")
                .title("웹프로그래밍")
                .build();

        Registration registration = Registration.builder()
                .id(1L)
                .member(member)
                .course(course)
                .build();

        given(memberRepository.findById(1L))
                .willReturn(Optional.of(member));

        given(registrationRepository.findAllByMember(member))
                .willReturn(List.of(registration));

        // when
        List<RegistrationResponse> result = registrationService.getMyCourses(1L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCourseCode()).isEqualTo("CS101");
        assertThat(result.get(0).getTitle()).isEqualTo("웹프로그래밍");
    }
}