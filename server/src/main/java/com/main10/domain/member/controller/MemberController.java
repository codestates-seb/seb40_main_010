package com.main10.domain.member.controller;

import com.main10.domain.member.dto.MemberDto;
import com.main10.domain.member.service.MemberService;
import com.main10.global.security.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.util.List;

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
    public ResponseEntity<?> postMember(@RequestBody @Valid MemberDto.Post post) {
        memberService.createMember(post);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 회원 정보 수정 컨트롤러 메서드
     * @param patch 회원 수정 정보
     * @param authentication 사용자 인증 정보
     * @return ResponseEntity
     * @author Quartz614
     */
    @PatchMapping("/edit")
    public ResponseEntity<?> patchMember(@RequestBody @Valid MemberDto.Patch patch,
                                      Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        memberService.updateMember(token.getId(), patch);
        return ResponseEntity.ok().build();
    }

    /**
     * 마이페이지 회원정보 조회
     * @param authentication 사용자 인증 정보
     * @return ResponseEntity
     * @author Quartz614
     */
    @GetMapping
    public ResponseEntity<MemberDto.Info> getMember(Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        MemberDto.Info member = memberService.getMember(token.getId());
        return ResponseEntity.ok(member);
    }

    /**
     * 회원 프로필 사진 업로드 S3 컨트롤러 메서드
     * @param authentication 사용자 인증 정보
     * @param file 프로필 사진 정보
     * @author LimJaeMinZ
     */
    @PostMapping("/profile")
    public ResponseEntity<?> createMemberImageS3(Authentication authentication,
                                    @RequestPart(value = "file") MultipartFile file) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        memberService.createProfileS3(token.getId(), file);
        return ResponseEntity.ok().build();
    }

    /**
     * 회원 프로필 사진 업로드 Local 메서드(Deprecated)
     * @param authentication 사용자 인증 정보
     * @param files 프로필 사진 정보
     * @author LimJaeMinZ
     */
    @Deprecated
    @PostMapping("/profileLocal")
    public ResponseEntity<?> createMemberImage(Authentication authentication,
                                  @RequestPart(value = "file") List<MultipartFile> files) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        memberService.createProfile(token.getId(), files);
        return ResponseEntity.ok().build();
    }
}