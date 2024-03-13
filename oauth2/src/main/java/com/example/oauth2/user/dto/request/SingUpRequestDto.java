package com.example.oauth2.user.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SingUpRequestDto {

    private final String socialId;
    private final String name;
    private final Long exp;
    private final int imageId;
    private final String nickname;


    @Builder
    private SingUpRequestDto(String socialId, String name, Long exp, int imageId, String nickname) {
        this.socialId = socialId;
        this.name = name;
        this.exp = exp;
        this.imageId = imageId;
        this.nickname = nickname;
    }

    public static SingUpRequestDto of(String socialId, String name, Long exp, int imageId, String nickname) {
        return builder()
                .socialId(socialId)
                .name(name)
                .exp(exp)
                .imageId(imageId)
                .nickname(nickname)
                .build();
    }
}
