package com.example.oauth2.oauth2.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProviderType {

    GOOGLE("google"),
    KAKAO("kakao"),
    ;

    private final String registrationId;
}
