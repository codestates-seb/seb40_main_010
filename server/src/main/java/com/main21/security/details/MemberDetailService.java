package com.main21.security.details;

import com.main21.exception.ExceptionCode;
import com.main21.member.entity.Member;
import com.main21.member.repository.MemberRepository;
import com.main21.security.exception.AuthException;
import com.main21.security.utils.CustomAuthorityUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final CustomAuthorityUtils authorityUtils;


    /**
     * 데이터베이스에서 유저 정보를 조회해 MemberDetails 클래스를 리턴해주는 오버라이딩 메서드
     * @param email 사용자 이메일
     * @return MemberDetails
     * @author mozzi327
     */
    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String email) {
        Member findMember = memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new AuthException(ExceptionCode.MEMBER_NOT_FOUND));

        return new MemberDetails(findMember);
    }


    /**
     * 멤버 테이블에서 사용할 Details 정보를 설정하는 클래스
     * @author mozzi327
     */
    private class MemberDetails extends Member implements UserDetails {
        MemberDetails(Member member) {
            setDetailsId(member.getId());
            setDetailsEamil(member.getEmail());
            setDetailsPassword(member.getPassword());
            setDetailsRoles(member.getRoles());
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorityUtils.createAuthorities(this.getRoles());
        }

        @Override
        public String getUsername() {
            return getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}

