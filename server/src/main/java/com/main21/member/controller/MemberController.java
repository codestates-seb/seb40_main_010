package com.main21.member.controller;

import com.main21.member.dto.MemberDto;
import com.main21.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static com.main21.member.utils.AuthConstant.REFRESH_TOKEN;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;


    /**
     * 회원가입 컨트롤러 메서드
     * @param post 회원가입 정보
     * @return ResponseEntity
     * @author Quartz614
     */
    @PostMapping("/join")
    public ResponseEntity postMember(@RequestBody MemberDto.Post post) {
        memberService.createMember(post);
        return new ResponseEntity(HttpStatus.CREATED);
    }


    /**
     * 회원 정보 수정 컨트롤러 메서드
     * @param patch 회원 수정 정보
     * @param refreshToken 리프레시 토큰
     * @return ResponseEntity
     * @author Quartz614
     */
    @PatchMapping("/edit")
    public ResponseEntity patchMember(@RequestBody MemberDto.Patch patch,
                                      @RequestHeader(name = REFRESH_TOKEN) String refreshToken) {
        memberService.updateMember(refreshToken, patch);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * 마이페이지 회원정보 조회
     * @param refreshToken 리프레시 토큰
     * @return ResponseEntity
     * @author Quartz614
     */
    @GetMapping
    public ResponseEntity getMember(@RequestHeader(name = REFRESH_TOKEN) String refreshToken) {
        MemberDto.Info member = memberService.getMember(refreshToken);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }


    /**
     * 회원 프로필 사진 업로드 S3 컨트롤러 메서드
     * @param refreshToken 리프레시 토큰
     * @param file 프로필 사진 정보
     * @author LimJaeMinZ
     */
    @PostMapping("/profile")
    public void createMemberImageS3(@RequestHeader(name = REFRESH_TOKEN) String refreshToken,
                                    @RequestPart(value = "file") MultipartFile file) {
        memberService.createProfileS3(refreshToken, file);
    }


    /**
     * 회원 프로필 사진 업로드 Local 메서드(Deprecated)
     * @param refreshToken 리프레시 토큰
     * @param files 프로필 사진 정보
     * @author LimJaeMinZ
     */
    @Deprecated
    @PostMapping("/profileLocal")
    public void createMemberImage(@RequestHeader(name = REFRESH_TOKEN) String refreshToken,
                                  @RequestPart(value = "file") List<MultipartFile> files) {
        memberService.createProfile(refreshToken, files);
    }
}