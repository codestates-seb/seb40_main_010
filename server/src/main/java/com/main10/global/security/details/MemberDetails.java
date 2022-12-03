package com.main10.global.security.details;

import com.main10.domain.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class MemberDetails implements UserDetails, OAuth2User {

    private final Member member;
    private Map<String, Object> attribute;

    /* 일반 로그인 사용자 */
    public MemberDetails(Member member) {
        this.member = member;
    }

    /* OAuth2 로그인 사용자 */
    public MemberDetails(Member member, Map<String, Object> attribute) {
        this.member = member;
        this.attribute = attribute;
    }

    /* 유저 권한 목록, 권한 반환*/
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return member.getRoles().stream()
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role)
                .collect(Collectors.toList());
    }

    /**
     * 계정 만료 여부
     * @return  true : 만료 안됨<br>
     *         false : 만료
     * @author mozzi327
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정 잠김 여부
     * @return  true : 잠기지 않음<br>
     *         false : 잠김
     * @author mozzi327
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }


    /**
     * 비밀번호 만료 여부
     * @return  true : 만료 안됨<br>
     *         false : 만료
     * @author mozzi327
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 사용자 활성화 여부
     * @return  true : 활성화 됨<br>
     *         false : 활성화 안됨
     * @author mozzi327
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * 사용자 비밀번호 정보 반환 메서드
     * @return password
     * @author mozzi327
     */
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    /**
     * 사용자 이름(이메일) 정보 반환 메서드
     * @return username(email)
     * @author mozzi327
     */
    @Override
    public String getUsername() {
        return member.getEmail();
    }

    /* --------OAuth2User 타입 오버라이딩 메서드 -------- */

    /**
     * OAuth2User 타입 오버라이딩 메서드
     * @return attributes
     * @author mozzi327
     */
    @Override
    public Map<String, Object> getAttributes() {
        return this.attribute;
    }

    /**
     * OAuth2User 닉네임 반환 메서드
     * @return email
     * @author mozzi327
     */
    @Override
    public String getName() {
        return member.getEmail();
    }
}
