package com.example.smartenroll.domain.registration.service;

import com.example.smartenroll.domain.course.entity.Course;
import com.example.smartenroll.domain.course.repository.CourseRepository;
import com.example.smartenroll.domain.member.entity.Member;
import com.example.smartenroll.domain.member.entity.MemberRoleEnum;
import com.example.smartenroll.domain.member.repository.MemberRepository;
import com.example.smartenroll.domain.registration.repository.RegistrationRepository;
import com.example.smartenroll.domain.student.entity.StudentMaster;
import com.example.smartenroll.domain.student.repository.StudentMasterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class RegistrationConcurrencyTest {

    private Course course;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private StudentMasterRepository studentMasterRepository;

    @BeforeEach
    void setUp() {

        registrationRepository.deleteAll();
        memberRepository.deleteAll();
        studentMasterRepository.deleteAll();
        courseRepository.deleteAll();

        course = courseRepository.save(
                Course.builder()
                        .courseCode("TEST001")
                        .title("테스트")
                        .capacity(30)
                        .currentCount(0)
                        .build()
        );
    }

    @Test
    void register() throws Exception {

        // 학생 100명 생성
        List<Member> members = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {

            StudentMaster student =
                    studentMasterRepository.save(
                            StudentMaster.builder()
                                    .studentNo(20250000L + i)
                                    .name("학생" + i)
                                    .build()
                    );

            Member member =
                    memberRepository.save(
                            Member.builder()
                                    .username("student" + i)
                                    .password("1234")
                                    .role(MemberRoleEnum.STUDENT)
                                    .studentMaster(student)
                                    .build()
                    );

            members.add(member);
        }

        int threadCount = 100;

        ExecutorService executorService =
                Executors.newFixedThreadPool(32);

        CountDownLatch latch =
                new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {

            Member member = members.get(i);

            executorService.submit(() -> {

                try {

                    registrationService.register(
                            member.getId(),
                            course.getId()
                    );

                } catch (Exception e) {

                    System.out.println(
                            "실패 : " + e.getMessage()
                    );

                } finally {

                    latch.countDown();
                }
            });
        }

        latch.await();

        Course result =
                courseRepository.findById(course.getId())
                        .orElseThrow();

        System.out.println(
                "현재 신청 인원 = "
                        + result.getCurrentCount()
        );

        System.out.println(
                "수강신청 수 = "
                        + registrationRepository.count()
        );

        assertEquals(
                30,
                result.getCurrentCount()
        );

        assertEquals(
                30,
                registrationRepository.count()
        );
    }
}