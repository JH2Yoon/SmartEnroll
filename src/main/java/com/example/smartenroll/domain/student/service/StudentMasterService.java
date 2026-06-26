package com.example.smartenroll.domain.student.service;

import com.example.smartenroll.common.exception.CustomException;
import com.example.smartenroll.common.exception.ErrorCode;
import com.example.smartenroll.domain.student.dto.request.StudentCreateRequest;
import com.example.smartenroll.domain.student.entity.StudentMaster;
import com.example.smartenroll.domain.student.repository.StudentMasterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentMasterService {

    private final StudentMasterRepository studentMasterRepository;

    public void create(StudentCreateRequest request) {

        if (studentMasterRepository.existsByStudentNo(request.getStudentNo())) {
            throw new CustomException(ErrorCode.STUDENT_ALREADY_EXISTS);
        }

        StudentMaster student = StudentMaster.builder()
                .studentNo(request.getStudentNo())
                .name(request.getName())
                .build();

        studentMasterRepository.save(student);
    }
}