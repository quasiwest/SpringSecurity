package com.example.oauthjwt.dto;

import java.util.Map;

public class GoogleResponse implements OAuth2Response{

    private final Map<String,Object> attribute;


    public GoogleResponse(Map<String,Object> attribute){
        // 구글은 특정 키안에 데이터가 있는게 아니여서 바로 키로 받아주면 됨
        this.attribute = attribute;
    }
    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return attribute.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getNickName() {
        return null;
    }
}
