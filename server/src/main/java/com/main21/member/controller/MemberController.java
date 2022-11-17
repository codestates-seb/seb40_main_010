package com.main21.member.controller;


import com.main21.member.dto.MemberDto;
import com.main21.member.entity.Member;
import com.main21.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    /**
     * 회원 프로필 사진 업로드 S3 (유저검증 필요)
     */
    @PostMapping("/profileS3")
    public void createMemberImageS3(@RequestPart(value = "file") MultipartFile file) throws Exception {

        memberService.createProfileS3(file);
    }


    /**
     * 회원 프로필 사진 업로드 Local (유저검증 필요)
     */
    @PostMapping("/profile")
    public void createMemberImage(@RequestPart(value = "file") List<MultipartFile> files) throws Exception {

        memberService.createProfile(files);
    }
}