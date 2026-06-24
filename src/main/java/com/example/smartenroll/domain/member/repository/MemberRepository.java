package com.example.smartenroll.domain.member.repository;

import com.example.smartenroll.domain.member.entity.Member;
import com.example.smartenroll.domain.member.entity.MemberRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsByRole(MemberRoleEnum memberRoleEnum);


}
