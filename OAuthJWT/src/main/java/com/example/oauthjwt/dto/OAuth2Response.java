package com.example.oauthjwt.dto;


/*
제공자(서비스)에 따라 데이터 제공 형식이 다름 -> Interface 구현 후 각 서비스 데이터 형식에 맞게 클래스를 만듬
 */
public interface OAuth2Response {
    //제공자(naver,google...)
    String getProvider();

    //제공자에서 발급해주는 아이디(번호)
    String getProviderId();

    //이메일
    String getEmail();

    //닉네임 초기값
    String getNickName();

}
