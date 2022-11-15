package com.main21.member.service;

import com.main21.member.repository.MemberRepository;
import com.main21.security.utils.JwtTokenUtils;
import com.main21.security.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final RedisUtils redisUtils;
    private final JwtTokenUtils jwtTokenUtils;


    /**
     * 사용자 로그아웃 비즈니스 로직 메서드
     * @param refreshToken 리프레시 토큰
     * @author mozzi327
     */
    public void logoutMember(Long memberId,
                             String refreshToken,
                             HttpServletResponse res) {
        Date expirationDate = jwtTokenUtils
                .getTokenExpiration(jwtTokenUtils.getRefreshTokenExpirationMinutes());

        res.addHeader("Set-Cookie", null);
    }
}
