package com.main10.domain.member.service;

import com.main10.domain.member.dto.TokenDto;
import com.main10.global.exception.ExceptionCode;
import com.main10.domain.member.dto.AuthDto;
import com.main10.domain.member.entity.Member;
import com.main10.global.security.dto.LoginDto;
import com.main10.global.security.exception.AuthException;
import com.main10.global.security.utils.JwtTokenUtils;
import com.main10.global.security.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

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

        memberDbService.isValid(findMember, login.getPassword());

        String generateAccessToken = jwtTokenUtils.generateAccessToken(findMember);
        String generateRefreshToken = jwtTokenUtils.generateRefreshToken(findMember);

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, generateAccessToken);
        headers.add(REFRESH_TOKEN, generateRefreshToken);

        redisUtils.setData(generateRefreshToken, findMember.getId(), jwtTokenUtils.getRefreshTokenExpirationMinutes());

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
     * @author mozzi327
     */
    public void logoutMember(String accessToken,
                             String refreshToken) {
        // accessToken parsing(Bearer ..)
        accessToken = jwtTokenUtils.parseAccessToken(accessToken);

        // 복호화가 가능한지 확인
        if (jwtTokenUtils.validateToken(accessToken))
            throw new AuthException(ExceptionCode.INVALID_AUTH_TOKEN);

        // refreshToken이 존재하는 경우 리프레시 토큰 삭제
        redisUtils.deleteData(refreshToken);

        // 엑세스 토큰 만료 전까지 블랙리스트 처리
        Long expiration = jwtTokenUtils.getExpiration(accessToken);
        redisUtils.setBlackList(accessToken, "Logout", expiration);
    }


    /**
     * 액세스 토큰 리이슈 메서드
     *
     * @param accessToken  액세스 토큰
     * @param refreshToken 리프레시 토큰
     * @return AuthDto.Response
     * @author mozzi327
     */
    public TokenDto.Response reIssueToken(String accessToken,
                                         String refreshToken) {
        // accessToken parsing(Bearer ..)
        accessToken = jwtTokenUtils.parseAccessToken(accessToken);

        // 복호화가 가능한지 확인
        if (jwtTokenUtils.validateToken(accessToken))
            throw new AuthException(ExceptionCode.INVALID_AUTH_TOKEN);

//        // refreshToken이 존재하지 않는 경우 예외를 던짐
//        if (redisUtils.getData(refreshToken) == null)
//            throw new AuthException(ExceptionCode.INVALID_AUTH_TOKEN);

        // 레디스에 저장된 Id 추출
        Long memberId = redisUtils.getId(refreshToken);

        // 액세스 토큰 발행
        Member findMember = memberDbService.ifExistsReturnMember(memberId);
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
}
