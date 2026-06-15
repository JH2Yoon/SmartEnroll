package com.example.smartenroll.domain.registration.repository;

import com.example.smartenroll.domain.course.entity.Course;
import com.example.smartenroll.domain.member.entity.Member;
import com.example.smartenroll.domain.registration.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    boolean existsByMemberAndCourse(Member member, Course course);
}
