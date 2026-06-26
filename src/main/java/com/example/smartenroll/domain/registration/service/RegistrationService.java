package com.example.smartenroll.domain.registration.service;

import com.example.smartenroll.common.exception.CustomException;
import com.example.smartenroll.common.exception.ErrorCode;
import com.example.smartenroll.domain.course.entity.Course;
import com.example.smartenroll.domain.course.repository.CourseRepository;
import com.example.smartenroll.domain.lectureTime.entity.LectureTime;
import com.example.smartenroll.domain.member.entity.Member;
import com.example.smartenroll.domain.member.repository.MemberRepository;
import com.example.smartenroll.domain.registration.dto.response.RegistrationResponse;
import com.example.smartenroll.domain.registration.entity.Registration;
import com.example.smartenroll.domain.registration.repository.RegistrationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final MemberRepository memberRepository;
    private final CourseRepository courseRepository;

    public void register(Long memberId, Long courseId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Course course = courseRepository.findByIdWithLock(courseId).orElseThrow(() ->
                new CustomException(ErrorCode.COURSE_NOT_FOUND));


        // 1. 중복 신청 체크
        if (registrationRepository.existsByMemberAndCourse(member, course)) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED);
        }

        // 2. 정원 체크
        if (course.isFull()) {
            throw new CustomException(ErrorCode.COURSE_FULL);
        }

        // 3. 시간표 충돌 체크
        List<Registration> registrations = registrationRepository.findAllByMember(member);

        for (Registration registration : registrations) {

            Course registeredCourse = registration.getCourse();

            for (LectureTime current :
                    registeredCourse.getLectureTimes()) {

                for (LectureTime target :
                        course.getLectureTimes()) {

                    if (isConflict(current, target)) {throw new CustomException(ErrorCode.TIMETABLE_CONFLICT);
                    }
                }
            }
        }

        Registration registration =
                Registration.builder()
                        .member(member)
                        .course(course)
                        .build();

        // 3. 신청 인원 증가
        course.increaseCount();

        registrationRepository.save(registration);
    }

    private boolean isConflict(LectureTime a, LectureTime b) {
        return a.getDayOfWeek() == b.getDayOfWeek()
                && a.getStartTime().isBefore(b.getEndTime())
                && b.getStartTime().isBefore(a.getEndTime());
    }

    public void cancel(Long memberId, Long registrationId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Registration registration = registrationRepository.findById(registrationId).orElseThrow(() ->
                new CustomException(ErrorCode.REGISTRATION_NOT_FOUND));

        if(!registration.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.REGISTRATION_FORBIDDEN);
        }

        registration.getCourse().decreaseCurrentCount();

        registrationRepository.delete(registration);
    }

    public List<RegistrationResponse> getMyCourses(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return registrationRepository.findAllByMember(member)
                .stream()
                .map(registration ->
                        RegistrationResponse.builder()
                                .registrationId(registration.getId())
                            .courseCode(registration.getCourse().getCourseCode())
                            .title(registration.getCourse().getTitle()
                            ).build()
                )
                .toList();
    }
}
