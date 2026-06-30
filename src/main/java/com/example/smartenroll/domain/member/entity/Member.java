package com.example.smartenroll.domain.member.entity;

import com.example.smartenroll.common.entiity.Timestamped;
import com.example.smartenroll.domain.registration.entity.Registration;
import com.example.smartenroll.domain.student.entity.StudentMaster;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member extends Timestamped {
    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 로그인 ID (ADMIN / STUDENT 공통)
    @Column(nullable = false, unique = true)
    private String username;

    // 비밀번호
    @Column(nullable = false)
    private String password;

    // 권한 (STUDENT / ADMIN)
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MemberRoleEnum role;

    // StudentMaster와 1:1 매핑
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_master_id")
    private StudentMaster studentMaster;

    // 선택적 (조회용)
    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Registration> registrations = new ArrayList<>();

    public void changePassword(String rawPassword, PasswordEncoder encoder) {
        this.password = encoder.encode(rawPassword);
    }
}