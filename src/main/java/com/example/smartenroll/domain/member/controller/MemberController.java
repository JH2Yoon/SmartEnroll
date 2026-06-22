package com.example.smartenroll.domain.member.controller;

import com.example.smartenroll.domain.member.dto.request.LoginRequest;
import com.example.smartenroll.domain.member.dto.request.RefreshRequest;
import com.example.smartenroll.domain.member.dto.request.SignupRequest;
import com.example.smartenroll.domain.member.dto.response.LoginResponse;
import com.example.smartenroll.domain.member.dto.response.SignupResponse;
import com.example.smartenroll.domain.member.dto.response.TokenResponse;
import com.example.smartenroll.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "학생/관리자", description = "학생/관리자 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/members")
public class MemberController {
    private final MemberService memberService;

    // 회원가입
    @Operation(summary = "회원 가입",
            description = "회원가입합니다."
    )
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody @Valid SignupRequest signupRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.signup(signupRequest));
    }

    @Operation(summary = "로그인",
            description = "로그인합니다."
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.login(loginRequest));
    }

    // 리프레쉬 토큰
    @Operation(
            summary = "토큰 재발급",
            description = "유효한 Refresh Token을 사용하여 Access Token 만료 시 새로운 토큰을 발급합니다."
    )
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(
            @RequestBody @Valid RefreshRequest request
    ) {
        return ResponseEntity.ok(
                memberService.refresh(request)
        );
    }

    // 로그아웃
    @Operation(
            summary = "로그아웃",
            description = "로그아웃합니다."
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication authentication) {

        Long memberId = (Long) authentication.getPrincipal();

        memberService.logout(memberId);

        return ResponseEntity.ok().build();
    }
}