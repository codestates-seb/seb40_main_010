package com.main21.security.utils;

import com.main21.exception.BusinessLogicException;
import com.main21.exception.ExceptionCode;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

import static com.main21.security.utils.AuthConstants.REFRESH_TOKEN;

@Service
public class CookieUtils {


    /**
     * ResponseCookie를 생성하는 메서드
     * @param cookieName 쿠키명
     * @param cookieValue 쿠키값
     * @param maxAgeSecond 쿠키 만료시간
     * @return ResponseCookie
     * @author mozzi327
     */
    public ResponseCookie createCookie(String cookieName,
                                       String cookieValue,
                                       long maxAgeSecond) {
        return ResponseCookie.from(cookieName, cookieValue)
                .maxAge(maxAgeSecond)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
    }


    /**
     * 전달받은 쿠키 값 중 RefreshToken이 있는지 확인하는 메서드<br>
     * 존재한다면 해당 토큰 값을 반환한다.
     * @param cookies (쿠키 배열)
     * @return 리프레시 토큰
     * @author mozzi327
     */
    public String isExistRefresh(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(REFRESH_TOKEN)) return cookie.getValue();
        }
        throw new BusinessLogicException(ExceptionCode.COOKIE_NOT_FOUND);
    }
}
