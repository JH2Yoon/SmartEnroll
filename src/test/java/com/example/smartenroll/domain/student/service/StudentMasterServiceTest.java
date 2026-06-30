package com.example.smartenroll.domain.student.service;

import com.example.smartenroll.common.exception.CustomException;
import com.example.smartenroll.common.exception.ErrorCode;
import com.example.smartenroll.domain.student.dto.request.StudentCreateRequest;
import com.example.smartenroll.domain.student.entity.StudentMaster;
import com.example.smartenroll.domain.student.repository.StudentMasterRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class StudentMasterServiceTest {

    @InjectMocks
    private StudentMasterService studentMasterService;

    @Mock
    private StudentMasterRepository studentMasterRepository;


    @Test
    @DisplayName("학생 등록 성공")
    void create() {

        // given
        StudentCreateRequest request = StudentCreateRequest.builder()
                .studentNo(20240001L)
                .name("홍길동")
                .build();

        given(studentMasterRepository.existsByStudentNo(20240001L))
                .willReturn(false);

        // when
        studentMasterService.create(request);

        // then
        then(studentMasterRepository)
                .should()
                .save(any(StudentMaster.class));
    }

    @Test
    @DisplayName("이미 등록된 학생이면 예외가 발생한다.")
    void createFail() {

        // given
        StudentCreateRequest request = StudentCreateRequest.builder()
                .studentNo(20240001L)
                .name("홍길동")
                .build();

        given(studentMasterRepository.existsByStudentNo(20240001L))
                .willReturn(true);

        // when & then
        assertThatThrownBy(() ->
                studentMasterService.create(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.STUDENT_ALREADY_EXISTS.getMessage());

        then(studentMasterRepository)
                .should(never())
                .save(any(StudentMaster.class));
    }
}