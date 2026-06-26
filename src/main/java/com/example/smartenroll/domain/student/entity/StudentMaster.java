package com.example.smartenroll.domain.student.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudentMaster {
    //PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 학번
    @Column(name = "student_no", nullable = false, unique = true)
    private Long studentNo;

    // 이름
    @Column(nullable = false)
    private String name;
}