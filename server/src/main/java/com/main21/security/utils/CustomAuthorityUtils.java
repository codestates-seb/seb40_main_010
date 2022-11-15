package com.main21.security.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomAuthorityUtils {

    @Value("${mail.address.admin}")
    private String addminMailAddress;

    private final List<GrantedAuthority> ADMIN_ROLES = AuthorityUtils
            .createAuthorityList("ROLE_ADMIN", "ROLE_USER");

    private final List<GrantedAuthority> USER_ROLES = AuthorityUtils
            .createAuthorityList("ROLE_USER");
    private final List<String> ADMIN_ROLES_STRING = List.of("ADMIN", "USER");
    private final List<String> USER_ROLES_STRING = List.of("USER");


    /**
     * 사용자 이메일을 통해 GrantedAuthority 컬렉션을 리턴해주는 메서드
     * @param email 사용자 이메일
     * @return List(GrantedAuthority)
     * @author mozzi327
     */
    public List<GrantedAuthority> createAuthorities(String email) {
        if (email.equals(addminMailAddress)) return ADMIN_ROLES;
        return USER_ROLES;
    }


    /**
     * 사용자 권한 리스트를 통해 GradtedAuthority 컬렉션을 리턴해주는 메서드
     * @param roles 사용자 권한 리스트
     * @return List(GrantedAuthority)
     * @author mozzi327
     */
    public List<GrantedAuthority> createAuthorities(List<String> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }


    /**
     * 사용자 이메일을 통해 데이터베이스에 저장된 유저 권한 리스트를 리턴해주는 메서드
     * @param email 사용자 이메일
     * @return List(String)
     * @author mozzi327
     */
    public List<String> createRoles(String email) {
        if (email.equals(addminMailAddress)) return ADMIN_ROLES_STRING;
        return USER_ROLES_STRING;
    }

}
