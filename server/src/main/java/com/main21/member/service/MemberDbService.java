package com.main21.member.service;

import com.main21.exception.BusinessLogicException;
import com.main21.exception.ExceptionCode;
import com.main21.member.dto.MemberDto;
import com.main21.member.entity.Member;
import com.main21.member.repository.MemberRepository;
import com.main21.security.exception.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 중복되는 메서드를 별도의 서비스 클래스로 만들어 관리
 * @author mozzi327
 */

@Service
@Transactional
@RequiredArgsConstructor
public class MemberDbService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    /**
     * 사용자 정보 조회 메서드
     *
     * @param memberId 사용자 식별자
     * @return Member
     * @author mozzi327
     */
    public Member ifExistsReturnMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }


    /**
     * 사용자 정보 조회 메서드(이메일)
     * @param email 사용자 이메일
     * @return Member
     * @author mozzi327
     */
    public Member ifExistMemberByEmail(String email) {
        return memberRepository
                .findMemberByEmail(email)
                .orElseThrow(() -> new AuthException(ExceptionCode.MEMBER_NOT_FOUND));
    }


    /**
     * 이메일 중복 검사 메서드
     * @param post 회원가입 정보
     * @author Quartz614
     */
    public void verifyEmail(MemberDto.Post post) {
        if (memberRepository.findByEmail(post.getEmail()).isPresent()) {
            throw new BusinessLogicException(ExceptionCode.EMAIL_ALREADY_EXIST); // 멤버로 바꿔야 ㅍ
        }
    }


    /**
     * 회원 정보 저장 메서드
     * @param member 사용자 정보
     * @author mozzi327
     */
    public void saveMember(Member member) {
        memberRepository.save(member);
    }


    /**
     * 비밀번호 일치 여부 확인 메서드
     * @param findMember 조회한 사용자 정보
     * @param password 로그인 시도 비밀번호
     * @author mozzi327
     */
    public void isValid(Member findMember, String password) {
        if (!passwordEncoder.matches(password, findMember.getPassword()))
            throw new AuthException(ExceptionCode.INVALID_MEMBER);
    }


    /**
     * 비밀번호 암호화 메서드
     * @param password 회원가입 비밀번호
     * @return String(인코딩된 패스워드)
     * @author mozzi327
     */
    public String encodingPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
