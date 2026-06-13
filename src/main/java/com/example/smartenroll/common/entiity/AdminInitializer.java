package com.example.smartenroll.common.entiity;

import com.example.smartenroll.domain.member.entity.Member;
import com.example.smartenroll.domain.member.entity.MemberRoleEnum;
import com.example.smartenroll.domain.member.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class AdminInitializer {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.initial.password}")
    private String password;

    @PostConstruct
    public void init() {

        if (memberRepository.existsByRole(MemberRoleEnum.ADMIN)) {
            return;
        }

        Member admin = Member.builder()
                .username("admin")
                .password(passwordEncoder.encode(password))
                .role(MemberRoleEnum.ADMIN)
                .build();

        memberRepository.save(admin);
    }
}