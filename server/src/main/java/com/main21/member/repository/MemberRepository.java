package com.main21.member.repository;

import com.main21.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select m from Member m where m.email= :email")
    Optional<Member> findByEmail(@Param("email") String email);

    Optional<Member> findMemberByEmail(String email);




}
