package com.example.demo.model.user.service;

import com.example.demo.security.oauth2.user.OAuth2Attributes;
import com.example.demo.security.oauth2.user.OAuth2UserInfoFactory;
import com.example.demo.model.user.model.OAuth2UserInfo;
import com.example.demo.model.user.model.User;
import com.example.demo.model.user.model.AuthProviderType;
import com.example.demo.model.user.repository.ReactiveOauth2UserInfoRepository;
import com.example.demo.model.user.repository.ReactiveUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class Oauth2UserServiceImp implements ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final ReactiveOauth2UserInfoRepository oAuth2UserRository;

    private final ReactiveUserRepository userRepository;

    @Override
    public Mono<OAuth2User> loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        final DefaultReactiveOAuth2UserService delegate = new DefaultReactiveOAuth2UserService();

        AuthProviderType authProviderType = AuthProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        return delegate.loadUser(userRequest).flatMap(oAuth2User -> {

            OAuth2Attributes userAttributes = OAuth2Attributes.of(authProviderType, oAuth2User.getAttributes());
            OAuth2UserInfo userInfo = OAuth2UserInfoFactory.of(authProviderType, oAuth2User.getAttributes());

            /**
             * OAtuh2 Provider의 필수데이터 확인
             * 서드파티 validation Check
             */
            if(userInfo.getEmail() == null) {
                OAuth2Error error = new OAuth2Error("providerNoData");
                return Mono.error(new Exception("OAUTH2공급자" + authProviderType +"에서  Email정보를 찾을수 없습니다."));
            }


            //TODO ...
            // 비지니스 로직 구현 Example
            oAuth2UserRository.findByProviderID(userInfo.getProviderID())
                    .flatMap(user -> {
                       return userRepository.findByEmail(user.getEmail())
                               .then(Mono.empty()); //OAuth2User 반환
            }).switchIfEmpty(Mono.defer(()->{
                User localUser = User.builder()
                                .email(userInfo.getEmail())
                                .mobile(userInfo.getMobile())
                                .build();
               return userRepository.save(localUser).flatMap(user->{
                    return oAuth2UserRository.save(userInfo)
                           .then(Mono.empty());    // OAuth2User 반환
                    });
                })
            );





            return Mono.just(oAuth2User);
        });
    }


}
