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
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    /**
     * 회원가입
     * @param post
     * @return
     */
    @PostMapping("/join")
    public ResponseEntity postMember(@RequestBody MemberDto.Post post) {
        memberService.createMember(post);
        log.info("post : {}", post);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * 회원 정보 수정
     * @param patch
     * @param memberId
     * @return
     */
    @PatchMapping("/edit")
    public ResponseEntity patchMember(@RequestBody MemberDto.Patch patch,
                                      @CookieValue(name = "memberId") Long memberId) {
        memberService.updateMember(memberId, patch);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 마이페이지 회원정보 조회
     * @param memberId
     * @return
     */
    @GetMapping
    public ResponseEntity getMembers(@CookieValue(name = "memberId") Long memberId) {
        MemberDto.Info member = memberService.getMember(memberId);
        return new ResponseEntity<>(member, HttpStatus.OK);
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