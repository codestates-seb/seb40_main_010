package com.main10.global.security.details;

import com.main10.global.exception.ExceptionCode;
import com.main10.domain.member.entity.AccountStatus;
import com.main10.domain.member.entity.Member;
import com.main10.domain.member.repository.MemberRepository;
import com.main10.global.security.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    /**
     * 데이터베이스에서 유저 정보를 조회해 MemberDetails 클래스를 리턴해주는 오버라이딩 메서드
     * @param email 사용자 이메일
     * @return MemberDetails
     * @author mozzi327
     */
    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String email) {
        Member findMember = memberRepository.findMemberByEmailAndAccountStatus(email, AccountStatus.COMMON_MEMBER)
                .orElseThrow(() -> new AuthException(ExceptionCode.MEMBER_NOT_FOUND));
        return new MemberDetails(findMember);
    }
}

