package com.main10.global.helper;

import com.main10.global.security.token.JwtAuthenticationToken;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.main10.domain.member.utils.AuthConstant.REFRESH_TOKEN;

@Controller
@AllArgsConstructor
public class MailController {

    private final MailService mailService;

    /**
     * 예약 확정(결제 완료) 내역 메일 전송
     *
     * @param reserveId 예약 식별자
     * @param refreshToken 리프래시 토큰
     * @return ResponseEntity
     * @author LeeGoh
     */
    @PostMapping("/reserve/{reserve-id}/mail")
    public ResponseEntity<?> execMail(@PathVariable("reserve-id") Long reserveId,
                                   Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        mailService.mailSend(token.getId(), reserveId);
        return ResponseEntity.ok().build();
    }
}
