package com.example.oauthjwt.dto;

import java.util.Map;

public class NaverResponse implements OAuth2Response{

    private final Map<String,Object> attribute;

    public NaverResponse(Map<String,Object> attribute){

        //naver는 response라는 키에 데이터가 담김
        this.attribute = (Map<String,Object>) attribute.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }


    @Override
    public String getNickName() {
        return attribute.get("nickname").toString();
    }


}
