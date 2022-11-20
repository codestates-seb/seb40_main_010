package com.main21.security.utils;

import com.main21.exception.BusinessLogicException;
import com.main21.exception.ExceptionCode;
import com.main21.member.entity.Member;
import com.main21.member.entity.Token;
import com.main21.member.repository.MemberRepository;
import com.main21.member.repository.TokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.*;

import static com.main21.security.utils.AuthConstants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

    @Getter
    @Value("${JWT_SECRET_KEY}")
    private String secretKey;

    @Getter
    @Value("${jwt.access-token-expiration-minutes}")
    private int accessTokenExpirationMinutes;

    @Getter
    @Value("${jwt.refresh-token-expiration-minutes}")
    private int refreshTokenExpirationMinutes;

    //    private final RedisUtils redisUtils;
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;


    /**
     * 서버 환경변수에 저장된 시크릿 키를 인코딩하여 반환해주는 메서드
     *
     * @param secretKey 시크릿 키
     * @return 인코딩된 시크릿 키
     * @author mozzi327
     */
    public String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }


    /**
     * 엑세스 토큰을 발급하는 메서드
     *
     * @param member 사용자 정보
     * @return String(액세스 토큰)
     * @author mozzi327
     */
    public String generateAccessToken(Member member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", member.getEmail());
        claims.put("roles", member.getRoles());

        String subject = member.getEmail();
        Date expiration = getTokenExpiration(getAccessTokenExpirationMinutes());
        String base64EncodedSecretKey = encodeBase64SecretKey(getSecretKey());

        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA).getTime())
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }


    /**
     * 리프레시 토큰을 발급하는 메서드
     *
     * @return String(리프레시 토큰)
     * @author mozzi327
     */
    public String generateRefreshToken(Member member) {
        String subject = member.getEmail();
        Date expiration = getTokenExpiration(getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = encodeBase64SecretKey(getSecretKey());

        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA).getTime())
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }


    /**
     * 검증 후 Jws(Claims) 정보를 반환해주는 메서드
     *
     * @param jws                    시그니처 정보
     * @param base64EncodedSecretKey base64 인코딩된 키
     * @return jws
     * @author mozzi327
     */
    public Jws<Claims> getClaims(String jws, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build().parseClaimsJws(jws);
    }

    public String getEmail(String accessToken) {
        if (accessToken.startsWith(BEARER)) {
            accessToken = accessToken.split(" ")[1];
        }

        Key key = getKeyFromBase64EncodedKey(encodeBase64SecretKey(secretKey));
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build().parseClaimsJws(accessToken);
        return (String) claims.getBody().get(USERNAME);
    }


    /**
     * 액세스 토큰의 만료시간을 반환해주는 메서드
     *
     * @param expirationMinutes 서버 지정 액세스 토큰 만료 시간
     * @return Date
     * @author mozzi327
     */
    public Date getTokenExpiration(int expirationMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expirationMinutes);
        return calendar.getTime();
    }


    public Long getExpiration(String accessToken) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(getKeyFromBase64EncodedKey(getSecretKey()))
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getExpiration();
        // 현재 시간
        long now = new Date().getTime();
        return (expiration.getTime() - now);
    }


    /**
     * base64로 인코딩된 키를 Key 객체로 만들어 반환하는 메서드
     *
     * @param base64EncodedSecretKey base64 인코딩된 키
     * @return Key
     * @author mozzi327
     */
    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    /**
     * 리프레시 토큰이 데이터베이스에 존재하는지 유무를 확인하는 메서드 (임시)<br>
     * - 리프레시 토큰 값이 같은지도 확인한다.
     *
     * @param refreshToken 리프레시 토큰
     * @author mozzi327
     */
    public void verifiedExistRefresh(String refreshToken) {

    }


    /**
     * 인증 성공 시 사용되는 사용자 정보(엔티티)를 반환하는 메서드<br>
     * - save를 한번 하는 이유는 최근 로그인 날짜를 갱신하기 위함
     *
     * @param email 사용자 이메일
     * @return 사용자 정보
     * @author mozzi327
     */
    public Member findMemberByEmail(String email) {
        Member findMember = memberRepository
                .findMemberByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        findMember.setLastModifiedAt(LocalDateTime.now()); // 마지막 로그인 날짜
        memberRepository.save(findMember);
        return findMember;
    }


    /**
     * 리프레시 토큰은 데이터베이스에 저장하는 메서드<br>
     * - 데이터베이스에 리프레시 토큰이 존재하는지 확인하고, 존재한다면 삭제후 저장
     *
     * @param refreshToken 리프레시 토큰
     * @param email        사용자 이메일
     * @param memberId     사용자 식별자
     * @author mozzi327
     */
    public void savedRefreshToken(String refreshToken, String email, Long memberId) {
        Optional<Token> findRefreshToken = tokenRepository.findTokenByMemberId(memberId);
        findRefreshToken.ifPresent(tokenRepository::delete);
        tokenRepository.save(Token.builder()
                .memberId(memberId)
                .memberEmail(email)
                .refreshToken(refreshToken)
                .build());
    }


    /**
     * 토큰 정보를 검증하는 메서드
     *
     * @param token 토큰 정보
     * @return boolean
     * @author mozzi327
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getKeyFromBase64EncodedKey(encodeBase64SecretKey(secretKey)))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }
}