package com.example.demo.model.user.repository;

import com.example.demo.model.user.model.OAuth2UserInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ReactiveOauth2UserInfoRepository extends ReactiveMongoRepository<OAuth2UserInfo, String> {
    Mono<OAuth2UserInfo> findByProviderID(String providerID);
}
