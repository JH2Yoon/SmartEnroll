package com.example.smartenroll.domain.lectureTime.repository;

import com.example.smartenroll.domain.lectureTime.entity.LectureTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureTimeRepository extends JpaRepository<LectureTime, Long> {

}
