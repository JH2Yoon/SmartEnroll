package com.example.smartenroll.domain.registration.controller;

import com.example.smartenroll.domain.registration.dto.request.RegistrationRequest;
import com.example.smartenroll.domain.registration.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
