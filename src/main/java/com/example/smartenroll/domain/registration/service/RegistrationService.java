package com.example.smartenroll.domain.registration.service;

import com.example.smartenroll.common.exception.CustomException;
import com.example.smartenroll.common.exception.ErrorCode;
import com.example.smartenroll.domain.course.entity.Course;
import com.example.smartenroll.domain.course.repository.CourseRepository;
import com.example.smartenroll.domain.member.entity.Member;
import com.example.smartenroll.domain.member.repository.MemberRepository;
import com.example.smartenroll.domain.registration.dto.request.RegistrationRequest;
import com.example.smartenroll.domain.registration.entity.Registration;
import com.example.smartenroll.domain.registration.repository.RegistrationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final MemberRepository memberRepository;
    private final CourseRepository courseRepository;

    public void register(Long memberId, RegistrationRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(() ->
                new CustomException(ErrorCode.COURSE_NOT_FOUND));


        // 1. 중복 신청 체크
        if (registrationRepository.existsByMemberAndCourse(member, course)) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED);
        }

        // 2. 정원 체크
        if (course.isFull()) {
            throw new CustomException(ErrorCode.COURSE_FULL);
        }

        Registration registration =
                Registration.builder()
                        .member(member)
                        .course(course)
                        .build();

        registrationRepository.save(registration);

        // 3. 신청 인원 증가
        course.increaseCount();
    }

}
