package com.main10.domain.member.service;

import com.main10.domain.member.dto.TokenDto;
import com.main10.global.exception.ExceptionCode;
import com.main10.domain.member.dto.AuthDto;
import com.main10.domain.member.entity.Member;
import com.main10.global.security.dto.LoginDto;
import com.main10.global.security.exception.AuthException;
import com.main10.global.security.utils.JwtTokenUtils;
import com.main10.global.security.utils.RedisUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Objects;
import static com.main10.global.security.utils.AuthConstants.AUTHORIZATION;
import static com.main10.global.security.utils.AuthConstants.REFRESH_TOKEN;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final RedisUtils redisUtils;
    private final JwtTokenUtils jwtTokenUtils;
    private final MemberDbService memberDbService;

    /**
     * 사용자 로그인 메서드
     *
     * @param login 로그인 정보
     * @return AuthDto.Response
     * @author mozzi327
     */
    public TokenDto.Response loginMember(LoginDto login) {
        Member findMember = memberDbService.ifExistMemberByEmail(login.getEmail());
        String provider = "common";

        memberDbService.isValid(findMember, login.getPassword());
        if (!Objects.isNull(redisUtils.getData(findMember.getEmail(), provider)))
            redisUtils.deleteData(findMember.getEmail(), findMember.getAccountStatus().getProvider());

        String generateAccessToken = jwtTokenUtils.generateAccessToken(findMember);
        String generateRefreshToken = jwtTokenUtils.generateRefreshToken(findMember);

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, generateAccessToken);
        headers.add(REFRESH_TOKEN, generateRefreshToken);

        redisUtils.setData(findMember.getEmail(), provider, generateRefreshToken, jwtTokenUtils.getRefreshTokenExpirationMinutes());

        AuthDto memberRes = AuthDto.builder()
                .nickname(findMember.getNickname())
                .email(login.getEmail())
                .mbti(findMember.getMbti())
                .build();

        return TokenDto.Response.builder()
                .response(memberRes)
                .headers(headers)
                .build();
    }

    /**
     * 사용자 로그아웃 비즈니스 로직 메서드
     *
     * @param accessToken  액세스 토큰
     * @param refreshToken 리프레시 토큰
     * @param memberId 사용자 식별자
     * @author mozzi327
     */
    public void logoutMember(String accessToken,
                             String refreshToken,
                             Long memberId) {

        Member findMember = memberDbService.ifExistsReturnMember(memberId);

        // accessToken parsing(Bearer ..)
        accessToken = jwtTokenUtils.parseAccessToken(accessToken);

        // 복호화가 가능한지 확인
        if (validateToken(accessToken))
            throw new AuthException(ExceptionCode.INVALID_AUTH_TOKEN);

        // refreshToken이 존재하는 경우 리프레시 토큰 삭제
        redisUtils.deleteData(findMember.getEmail(), findMember.getAccountStatus().getProvider());

        // 엑세스 토큰 만료 전까지 블랙리스트 처리
        Long expiration = jwtTokenUtils.getExpiration(accessToken);
        redisUtils.setBlackList(accessToken, "Logout", expiration);
    }


    /**
     * 액세스 토큰 리이슈 메서드
     *
     * @param accessToken  액세스 토큰
     * @param refreshToken 리프레시 토큰
     * @param memberId 사용자 식별자
     * @return AuthDto.Response
     * @author mozzi327
     */
    public TokenDto.Response reIssueToken(String accessToken,
                                          String refreshToken,
                                          Long memberId) {
        Member findMember = memberDbService.ifExistsReturnMember(memberId);

        // accessToken parsing(Bearer ..)
        accessToken = jwtTokenUtils.parseAccessToken(accessToken);

        // 복호화가 가능한지 확인
        if (validateToken(accessToken))
            throw new AuthException(ExceptionCode.INVALID_AUTH_TOKEN);

//        // refreshToken이 존재하지 않는 경우 예외를 던짐
        if (redisUtils.getData(findMember.getEmail(), findMember.getAccountStatus().getProvider()) == null)
            throw new AuthException(ExceptionCode.INVALID_AUTH_TOKEN);

        // 액세스 토큰 발행
        String generateToken = jwtTokenUtils.generateAccessToken(findMember);

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, generateToken);
        headers.add(REFRESH_TOKEN, refreshToken);

        AuthDto memberRes = AuthDto.builder()
                .nickname(findMember.getNickname())
                .email(findMember.getEmail())
                .mbti(findMember.getMbti())
                .build();

        return TokenDto.Response.builder()
                .headers(headers)
                .response(memberRes)
                .build();
    }

    /**
     * 토큰 정보를 검증하는 메서드
     *
     * @param token 토큰 정보
     * @return boolean
     * @author mozzi327
     */
    private boolean validateToken(String token) {
        String base64EncodedSecretKey = jwtTokenUtils.encodeBase64SecretKey(jwtTokenUtils.getSecretKey());
        Key key = jwtTokenUtils.getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return false;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return true;
    }
}
