package com.example.smartenroll.domain.member.entity;

import com.example.smartenroll.common.entiity.Timestamped;
import com.example.smartenroll.domain.registration.entity.Registration;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends Timestamped {
    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 학번
    @Column(name = "student_no", nullable = false, unique = true)
    private String studentNo;

    // 비밀번호
    @Column(nullable = false)
    private String password;

    // 이름
    @Column(nullable = false)
    private String name;

    // 권한 (STUDENT / ADMIN)
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MemberRoleEnum role;

    // 선택적 (조회용)
    @OneToMany(mappedBy = "member")
    private List<Registration> registrations = new ArrayList<>();

    public void changePassword(String rawPassword, PasswordEncoder encoder) {
        this.password = encoder.encode(rawPassword);
    }
}