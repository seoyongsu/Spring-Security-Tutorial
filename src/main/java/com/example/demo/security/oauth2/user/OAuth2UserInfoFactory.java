package com.example.demo.security.oauth2.user;

import com.example.demo.model.user.model.OAuth2UserInfo;
import com.example.demo.model.user.model.AuthProviderType;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo of(AuthProviderType authProviderType, Map<String, Object> attributes) {
        switch (authProviderType) {
            case KAKAO : return ofKakao(attributes);
            case NAVER : return ofNaver(attributes);
            //        case GOOGLE : return new GoogleOAuth2UserInfo(attributes);
            default: throw new IllegalArgumentException("Invalid Provider Type.");
        }

    }

    @SuppressWarnings("unchecked")
    private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
        // kakao는 kakao_account에 유저정보가 있다. (email)
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
        // kakao_account안에 또 profile이라는 JSON객체가 있다. (nickname, profile_image)
        Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");

        return OAuth2UserInfo.builder()
                .providerID(attributes.get("id").toString())
                .email((String) kakaoAccount.get("email"))
                .mobile(null)
                .name((String) kakaoProfile.get("nickname"))
                .image((String) kakaoProfile.get("thumbnail_image"))
                .authProviderType(AuthProviderType.NAVER)
                .build();

    }

    @SuppressWarnings("unchecked")
    private static OAuth2UserInfo ofNaver(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuth2UserInfo.builder()
                .providerID((String) response.get("id"))
                .email((String) response.get("email"))
                .mobile( (String) response.get("mobile") )
                .name((String) response.get("name"))
                .image((String) response.get("profile_image"))
                .authProviderType(AuthProviderType.NAVER)
                .build();

    }
}
