package com.example.smartenroll.domain.lectureTime.entity;

import com.example.smartenroll.domain.course.entity.Course;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureTime {
    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // N : 1 (Course)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // 요일
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    // 시작 시간
    private LocalTime startTime;

    // 종료 시간
    private LocalTime endTime;

    public void validate() {
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Invalid time range");
        }
    }
}
