package com.example.smartenroll.domain.registration.controller;

import com.example.smartenroll.domain.registration.dto.response.RegistrationResponse;
import com.example.smartenroll.domain.registration.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "수강신청", description = "수강신청 API")
@RestController
@RequestMapping("/v1/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @Operation(summary = "수강신청",
            description = "강의를 신청합니다."
    )
    @PostMapping("/{courseId}")
    public ResponseEntity<Void> register(Authentication authentication, @PathVariable Long courseId) {

        Long memberId = (Long) authentication.getPrincipal();
        registrationService.register(memberId, courseId);

        return ResponseEntity.ok().build();
    }

    // 수강취소
    @Operation(summary = "수강취소",
            description = "강의를 취소합니다."
    )
    @DeleteMapping("/{registrationId}")
    public ResponseEntity<Void> cancel(Authentication authentication, @PathVariable Long registrationId) {

        Long memberId = (Long) authentication.getPrincipal();

        registrationService.cancel(memberId, registrationId);

        return ResponseEntity.noContent().build();
    }

    // 내 수강목록 조회
    @Operation(summary = "내 수강목록 조회",
            description = "수강한 목록을 조회합니다."
    )
    @GetMapping("/me")
    public ResponseEntity<List<RegistrationResponse>> getMyCourses(Authentication authentication) {

        Long memberId = (Long) authentication.getPrincipal();

        return ResponseEntity.ok(registrationService.getMyCourses(memberId));
    }
}
