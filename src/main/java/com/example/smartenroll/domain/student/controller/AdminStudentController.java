package com.example.smartenroll.domain.student.controller;

import com.example.smartenroll.domain.student.dto.request.StudentCreateRequest;
import com.example.smartenroll.domain.student.service.StudentMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/students")
public class AdminStudentController {

    private final StudentMasterService studentMasterService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody StudentCreateRequest request) {
        studentMasterService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
