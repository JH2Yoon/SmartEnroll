package com.example.smartenroll.domain.registration.controller;

import com.example.smartenroll.domain.registration.dto.request.RegistrationRequest;
import com.example.smartenroll.domain.registration.dto.response.RegistrationResponse;
import com.example.smartenroll.domain.registration.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    // 수강신청
    @PostMapping
    public ResponseEntity<Void> register(Authentication authentication, @RequestBody RegistrationRequest request) {

        Long memberId = (Long) authentication.getPrincipal();
        registrationService.register(memberId, request);

        return ResponseEntity.ok().build();
    }

    // 수강취소
    @DeleteMapping("/{registrationId}")
    public ResponseEntity<Void> cancel(Authentication authentication, @PathVariable Long registrationId) {

        Long memberId = (Long) authentication.getPrincipal();

        registrationService.cancel(memberId, registrationId);

        return ResponseEntity.noContent().build();
    }

    // 내 수강목록 조회
    @GetMapping("/me")
    public ResponseEntity<List<RegistrationResponse>> getMyCourses (Authentication authentication) {

        Long memberId = (Long) authentication.getPrincipal();

        return ResponseEntity.ok(registrationService.getMyCourses(memberId));
    }
}
