package com.main21.helper;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.main21.member.utils.AuthConstant.REFRESH_TOKEN;

@Controller
@AllArgsConstructor
public class MailController {

    private final MailService mailService;

    @GetMapping("/mail")
    public String dispMail() {
        return "mail";
    }

    @PostMapping("/reserve/{reserve-id}/mail")
    public ResponseEntity execMail(@PathVariable("reserve-id") Long reserveId,
                                   @RequestHeader(REFRESH_TOKEN) String refreshToken) {
        mailService.mailSend(refreshToken, reserveId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
