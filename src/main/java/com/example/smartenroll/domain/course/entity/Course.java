package com.example.smartenroll.domain.course.entity;

import com.example.smartenroll.domain.lectureTime.entity.LectureTime;
import com.example.smartenroll.domain.registration.entity.Registration;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 강의 코드
    @Column(name = "course_code", nullable = false, unique = true)
    private String courseCode;

    // 강의 이름
    @Column(nullable = false)
    private String title;

    // 	정원
    @Column(nullable = false)
    private Integer capacity;

    // 현재 신청 인원
    @Column(name = "current_count", nullable = false)
    private Integer currentCount;

    // 수강신청
    @OneToMany(mappedBy = "course")
    private List<Registration> registrations = new ArrayList<>();

    // 강의 시간
    @OneToMany(mappedBy = "course")
    private List<LectureTime> lectureTimes = new ArrayList<>();

    public boolean isFull() {
        return currentCount >= capacity;
    }

    public void increaseCount() {
        this.currentCount++;
    }
}
