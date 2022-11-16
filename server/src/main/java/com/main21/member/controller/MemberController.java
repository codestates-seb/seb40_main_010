package com.main21.member.controller;


import com.main21.member.dto.MemberDto;
import com.main21.member.entity.Member;
import com.main21.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity postMember(@RequestBody MemberDto.Post post) {
        memberService.createMember(post);
        log.info("post : {}", post);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PatchMapping("/edit")
    public ResponseEntity patchMember(@RequestBody MemberDto.Patch patch,
                                      @CookieValue(name = "memberId") Long memberId) {
        memberService.updateMember(memberId, patch);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getMember(@CookieValue(name = "memberId") Long memberId) {
        Member member = memberService.getMember(memberId);
        return new ResponseEntity(member, HttpStatus.OK);
    }
}