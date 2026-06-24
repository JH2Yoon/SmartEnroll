package com.example.smartenroll.domain.member.service;

import com.example.smartenroll.common.exception.CustomException;
import com.example.smartenroll.common.exception.ErrorCode;
import com.example.smartenroll.common.jwt.JwtUtil;
import com.example.smartenroll.domain.member.dto.request.LoginRequest;
import com.example.smartenroll.domain.member.dto.request.RefreshRequest;
import com.example.smartenroll.domain.member.dto.request.SignupRequest;
import com.example.smartenroll.domain.member.dto.response.LoginResponse;
import com.example.smartenroll.domain.member.dto.response.SignupResponse;
import com.example.smartenroll.domain.member.dto.response.TokenResponse;
import com.example.smartenroll.domain.member.entity.Member;
import com.example.smartenroll.domain.member.entity.MemberRoleEnum;
import com.example.smartenroll.domain.member.repository.MemberRepository;
import com.example.smartenroll.domain.student.entity.StudentMaster;
import com.example.smartenroll.domain.student.repository.StudentMasterRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final StudentMasterRepository studentMasterRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public SignupResponse signup(SignupRequest request) {

        StudentMaster student = studentMasterRepository.findByStudentNo(request.getStudentNo())
                .orElseThrow(() -> new CustomException(ErrorCode.STUDENT_NOT_FOUND));

        String username = String.valueOf(student.getStudentNo());

        if (memberRepository.existsByUsername(username)) {
            throw new CustomException(ErrorCode.MEMBER_ALREADY_EXISTS);
        }

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(MemberRoleEnum.STUDENT)
                .studentMaster(student)
                .build();

        memberRepository.save(member);

        return new SignupResponse(student.getName(), student.getStudentNo());
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        Member member = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        String accessToken =
                jwtUtil.createAccessToken(
                        member.getId(),
                        member.getRole()
                );

        String refreshToken =
                jwtUtil.createRefreshToken(
                        member.getId()
                );

        redisTemplate.opsForValue().set(
                "RT:" + member.getId(),
                refreshToken,
                7,
                TimeUnit.DAYS
        );

        return new LoginResponse(
                accessToken,
                refreshToken,
                member.getRole()
        );
    }

    @Transactional(readOnly = true)
    public TokenResponse refresh(RefreshRequest request) {

        Claims claims =
                jwtUtil.parseClaims(
                        request.getRefreshToken()
                );

        Long memberId =
                Long.valueOf(claims.getSubject());

        String savedToken =
                redisTemplate.opsForValue().get(
                        "RT:" + memberId
                );

        if (savedToken == null ||
                !savedToken.equals(request.getRefreshToken())) {

            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        Member member =
                memberRepository.findById(memberId)
                        .orElseThrow(
                                () -> new CustomException(
                                        ErrorCode.MEMBER_NOT_FOUND
                                )
                        );

        String accessToken =
                jwtUtil.createAccessToken(
                        member.getId(),
                        member.getRole()
                );

        return new TokenResponse(accessToken);
    }

    @Transactional
    public void logout(Long memberId) {
        redisTemplate.delete(
                "RT:" + memberId
        );
    }
}
