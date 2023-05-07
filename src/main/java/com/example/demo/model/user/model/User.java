package com.example.demo.model.user.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Map;

@Getter
@Document
public class User {
    @Id
    private String id;

    @Indexed(unique = true)
    private String userID;

    @Indexed(unique = true)
    private String email;

    private String mobile;

    private String password;

    private String name;

    private AuthProviderType authProviderType;

    /**
     * OAuth2 UserInfo data
     * Key   : AuthProvider
     * value : OAuth2UserInfo
     */
    private Map<AuthProviderType, Object> oAuth2UserInfo;

    @Builder
    public User(String userID, String email, String mobile, String password, String name,  Map<AuthProviderType,Object> oAuth2UserInfo) {
        this.userID = userID;
        this.password = password;
        this.mobile = mobile;
        this.email = email;
        this.name = name;
        this.authProviderType = AuthProviderType.LOCAL;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }
}
