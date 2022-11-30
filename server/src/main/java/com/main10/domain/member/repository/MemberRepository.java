package com.main10.domain.member.repository;

import com.main10.domain.member.entity.AccountStatus;
import com.main10.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select m from Member m where m.email= :email")
    Optional<Member> findByEmail(@Param("email") String email);

    Optional<Member> findMemberByEmail(String email);

    Optional<Member> findMemberByNickname(String nickname);

    Optional<Member> findMemberByEmailAndAccountStatus(String email, AccountStatus accountStatus);

}
