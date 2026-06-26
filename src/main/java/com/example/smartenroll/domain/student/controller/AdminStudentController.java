package com.example.smartenroll.domain.student.controller;

import com.example.smartenroll.domain.student.dto.request.StudentCreateRequest;
import com.example.smartenroll.domain.student.service.StudentMasterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "관리자 학생 관리", description = "관리자의 학생 정보 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/students")
public class AdminStudentController {

    private final StudentMasterService studentMasterService;

    @Operation(summary = "학생 등록",
            description = "관리자가 학생을 등록합니다."
    )
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody StudentCreateRequest request) {
        studentMasterService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
