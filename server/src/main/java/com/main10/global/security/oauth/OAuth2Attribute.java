package com.main10.global.security.oauth;

import com.main10.global.security.utils.AuthConstants;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;

/**
 * OAuth2 공급자별 Attributes 엔티티 생성 클래스
 * @author mozzi327
 */
@Slf4j
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class OAuth2Attribute {
    private Map<String, Object> attributes;
    private String attributeKey;
    private String email;
    private String name;
    private String picture;
    private final String provider;

    /**
     * OAuth2Attribute of 메서드
     * @param provider OAuth2 인증 공급자
     * @param attributeKey 데이터 카테고리
     * @param attributes 사용자 데이터
     * @return OAuth2Attribute
     * @author mozzi327
     */
    public static OAuth2Attribute of(String provider, String attributeKey,
                              Map<String, Object> attributes) {
        switch (provider) {
            case AuthConstants.GOOGLE:
                return ofGoogle(attributeKey, attributes);
            case AuthConstants.KAKAO:
                return ofKakao("id", attributes);
            case AuthConstants.NAVER:
                return ofNaver("id", attributes);
            default:
                throw new RuntimeException();
        }
    }

    /**
     * OAuth2 Google OAuth2Attribute 생성 메서드
     * @param attributeKey 데이터 카테고리
     * @param attributes 사용자 데이터
     * @return OAuth2Attribute
     * @author mozzi328
     */
    private static OAuth2Attribute ofGoogle(String attributeKey,
                                            Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String)attributes.get("picture"))
                .attributes(attributes)
                .attributeKey(attributeKey)
                .provider(AuthConstants.GOOGLE)
                .build();
    }

    /**
     * OAuth2 Kakao OAuth2Attribute 생성 메서드
     * @param attributeKey 데이터 카테고리
     * @param attributes 사용자 정보
     * @return OAuth2Attribute
     * @author mozzi327
     */
    private static OAuth2Attribute ofKakao(String attributeKey,
                                           Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuth2Attribute.builder()
                .name((String) kakaoProfile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .picture((String)kakaoProfile.get("profile_image_url"))
                .attributes(attributes)
                .attributeKey(attributeKey)
                .provider(AuthConstants.KAKAO)
                .build();
    }

    /**
     * OAuth2 Naver OAuth2Attribute 생성 메서드
     * @param attributeKey 데이터 카테고리
     * @param attributes 사용자 데이터
     * @return OAuth2Attribute
     * @author mozzi327
     */
    private static OAuth2Attribute ofNaver(String attributeKey,
                                           Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuth2Attribute.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .attributeKey(attributeKey)
                .provider(AuthConstants.NAVER)
                .build();
    }
}
