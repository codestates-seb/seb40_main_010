package com.main21.member.repository;

import com.main21.member.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findTokenByMemberId(Long memberId);
    void deleteTokenByMemberId(Long memberId);
}
