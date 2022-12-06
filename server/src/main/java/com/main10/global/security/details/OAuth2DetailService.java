package com.main10.global.security.details;

import com.main10.domain.member.entity.AccountStatus;
import com.main10.domain.member.entity.Member;
import com.main10.domain.member.entity.MemberImage;
import com.main10.domain.member.repository.MemberRepository;
import com.main10.global.security.oauth.OAuth2Attribute;
import com.main10.global.security.utils.CustomAuthorityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * OAuth2UserService를 구현한 커스텀 OAuth2UserService 클래스
 * @author mozzi327
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2DetailService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;
    private final CustomAuthorityUtils authorityUtils;
    @Value("${default.image.address}")
    private String defaultImageAddress;

    /**
     * 로그인 성공 후 토큰 발급을 위한 OAuth2 객체 반환 메서드
     * @param userRequest 사용자 정보
     * @return OAuth2User
     * @throws OAuth2AuthenticationException OAuth2 인증 예외
     * @author mozzi327
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String attributeKey = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuth2Attribute oAuth2Attribute = OAuth2Attribute
                .of(provider, attributeKey, oAuth2User.getAttributes());

        String profileImage = oAuth2Attribute.getPicture();

        String email = oAuth2Attribute.getEmail();
        String nickname = oAuth2Attribute.getName();
        Member oAuthMember = saveMemberIfNotExist(email, nickname, provider, profileImage);

        return new MemberDetails(oAuthMember, oAuth2Attribute.getAttributes());
    }

    /**
     * 회원가입 / 로그인을 위한 저장 메서드
     * @param email 사용자 이메일
     * @param nickname 사용자 닉네임
     * @param provider OAuth2 인증 공급자
     * @return savedMember
     * @author mozzi327
     */
    private Member saveMemberIfNotExist(String email, String nickname, String provider, String profileImage) {
        Optional<Member> findMember = memberRepository
                .findMemberByEmailAndAccountStatus(email, AccountStatus.whatIsProvider(provider));
        if (findMember.isPresent()) return findMember.get(); // 기존 계정이 존재할 경우 기존 계정 리턴
        List<String> roles = authorityUtils.createRoles(email, provider);
        Member newMember = Member.builder()
                .email(email)
                .nickname(nickname)
                .roles(roles)
                .mbti("NONE")
                .accountStatus(AccountStatus.whatIsProvider(provider))
                .build();

        MemberImage memberImageS3 = MemberImage.builder()
                .filePath(profileImage)
                .build();
        newMember.addMemberImage(memberImageS3);

        return memberRepository.save(newMember);
    }
}
