package com.main21.member.service;

import com.main21.exception.ExceptionCode;
import com.main21.member.dto.AuthDto;
import com.main21.member.entity.Member;
import com.main21.member.entity.Token;
import com.main21.member.repository.MemberRepository;
import com.main21.member.repository.TokenRepository;
import com.main21.security.exception.AuthException;
import com.main21.security.utils.JwtTokenUtils;
import com.main21.security.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenUtils jwtTokenUtils;
    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;
    private final RedisUtils redisUtils;


    /**
     * 사용자 로그아웃 비즈니스 로직 메서드
     * @param accessToken 액세스 토큰
     * @param refreshToken 리프레시 토큰
     * @author mozzi327
     */
    public void logoutMember(String accessToken,
                             String refreshToken) {
        if (!jwtTokenUtils.validateToken(accessToken))
            throw new AuthException(ExceptionCode.INVALID_AUTH_TOKEN);

        if (redisUtils.getData(refreshToken) != null) {
            redisUtils.deleteData(refreshToken);
        }

        Long expiration = jwtTokenUtils.getExpiration(accessToken);
        redisUtils.setBlackList(accessToken, expiration);
    }


    /**
     * 사용자 로그아웃 비즈니스 로직 메서드(테스트)
     *
     * @param refreshToken 리프레시 토큰
     * @author mozzi327
     */
    public void logoutMemberByDb(Long memberId,
                                 String refreshToken,
                                 HttpServletResponse res) {
        Token findToken = ifExistToken(memberId);
        tokenRepository.delete(findToken);
        res.addHeader("Set-Cookie", null);
        checkTokenValidation(findToken, refreshToken);
    }


    /**
     * 액세스 토큰 리이슈 메서드
     * @param refreshToken 리프레시 토큰
     * @param memberId 사용자 식별자
     * @return AuthDto.Response
     * @author mozzi327
     */
    public AuthDto.Response reIssueToken(Long memberId,
                             String refreshToken) {
        Token findToken = ifExistToken(memberId);
        checkTokenValidation(findToken, refreshToken);

        Member findMember = ifExistMember(memberId);
        AuthDto.Token generateToken = createReIssueToken(findMember, refreshToken);

        AuthDto.Response response = AuthDto.Response.builder()
                .accessToken(generateToken.getAccessToken())
                .nickname(findMember.getNickname())
                .email(findMember.getEmail())
                .build();

        tokenRepository.deleteTokenByMemberId(memberId);
        tokenRepository.save(Token.builder()
                .refreshToken(generateToken.getRefreshToken())
                .memberEmail(findMember.getEmail())
                .memberId(findMember.getId())
                .build());

        return response;
    }


    /**
     * 토큰 Response 생성 메서드
     * @param findMember 사용자 정보
     * @param refreshToken 리프레시 토큰
     * @return AuthDto.Token(액세스 토큰, 리프레시 토큰)
     * @author mozzi327
     */
    private AuthDto.Token createReIssueToken(Member findMember,
                                             String refreshToken) {
        String accessToken = jwtTokenUtils.generateAccessToken(findMember);
        return AuthDto.Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    /**
     * 리프레시 토큰 데이터베이스 존재 유무 확인 메서드
     *
     * @param memberId 사용자 식별자
     * @return RefreshToken(리프레시토큰)
     * @author mozzi327
     */
    private Token ifExistToken(Long memberId) {
        return tokenRepository
                .findTokenByMemberId(memberId)
                .orElseThrow(() -> new AuthException(ExceptionCode.REFRESH_TOKEN_NOT_FOUND));
    }


    /**
     * 사용자 식별자를 통해 사용자 정보가 존재하는지 확인하는 메서드
     *
     * @param memberId 사용자 식별자
     * @return Member
     * @author mozzi327
     */
    public Member ifExistMember(Long memberId) {
        return memberRepository
                .findById(memberId)
                .orElseThrow(() -> new AuthException(ExceptionCode.MEMBER_NOT_FOUND));
    }


    /**
     * 쿠키 토큰과 DB 저장 토큰 값이 일치하는지 확인하는 메서드
     * @param findToken DB 저장 토큰
     * @param refreshToken 쿠키 토큰
     * @author mozzi327
     */
    public void checkTokenValidation(Token findToken, String refreshToken) {
        if (!findToken.getRefreshToken().equals(refreshToken))
            throw new AuthException(ExceptionCode.INVALID_REFRESH_TOKEN);
    }
}
