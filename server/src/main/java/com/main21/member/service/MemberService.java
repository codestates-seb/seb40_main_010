package com.main21.member.service;

import com.main21.exception.BusinessLogicException;
import com.main21.exception.ExceptionCode;
import com.main21.member.dto.MemberDto;
import com.main21.member.entity.Member;
import com.main21.member.repository.MemberRepository;
import com.main21.security.utils.CustomAuthorityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;


    public void createMember(MemberDto.Post post) {
        verifyEmail(post);
        List<String> roles = authorityUtils.createRoles(post.getEmail());

        Member member = Member.builder()
                .email(post.getEmail())
                .password(passwordEncoder.encode(post.getPassword()))
                .nickname(post.getNickname())
                .phoneNumber(post.getPhoneNumber())
                .roles(roles)
                .mbti(post.getMbti())
                .build();


        memberRepository.save(member);
    }
    public void updateMember(Long memberId, MemberDto.Patch patch) {
        Member findMember = findVerifyMember(memberId);
        findMember.editMember(patch.getNickname(), patch.getMbti());
        memberRepository.save(findMember);
    }
    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() ->new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    // 이미 존재하는 회원 파악
    public Member findVerifyMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.PLACE_NOT_FOUND)); // 멤버로 수정해야함
    }

    public void verifyEmail(MemberDto.Post post) {
        if (memberRepository.findByEmail(post.getEmail()).isPresent()) {
            throw new BusinessLogicException(ExceptionCode.PLACE_NOT_FOUND); // 멤버로 바꿔야 ㅍ
        }
    }
}
