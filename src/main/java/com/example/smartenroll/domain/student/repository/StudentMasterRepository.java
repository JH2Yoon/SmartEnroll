package com.example.smartenroll.domain.student.repository;

import com.example.smartenroll.domain.student.entity.StudentMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentMasterRepository extends JpaRepository<StudentMaster, Long> {
    Optional<StudentMaster> findByStudentNo(Long studentNo);

    boolean existsByStudentNo(Long studentNo);
}