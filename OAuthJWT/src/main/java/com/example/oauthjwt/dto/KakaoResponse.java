package com.example.oauthjwt.dto;

import java.util.Map;

public class KakaoResponse implements OAuth2Response{
    private final Map<String,Object> attribute;

    public KakaoResponse(Map<String,Object> attribute){
        // 구글은 특정 키안에 데이터가 있는게 아니여서 바로 키로 받아주면 됨
        this.attribute = attribute;
    }
    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return (String) ((Map<String, Object>) attribute.get("kakao_account")).get("email");
    }

    @Override
    public String getNickName() {
        return (String) ((Map<String, Object>)((Map<String, Object>) attribute.get("kakao_account")).get("profile")).get("nickname");
    }
}
