package com.example.oauthjwt.service;

import com.example.oauthjwt.dto.*;
import com.example.oauthjwt.entity.User;
import com.example.oauthjwt.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if(registrationId.equals("naver")){

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
//        else if(registrationId.equals("google")){
//
//            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
//        }
        else if(registrationId.equals("kakao")){

            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }
        else{
            return null;
        }
        //우리 서버에서 특정할 수 있는 username 생성
        String socialId = oAuth2Response.getProvider() +" " + oAuth2Response.getProviderId();

        User existData = userRepository.findBySocialId(socialId);

        if(existData == null){

            User user = User.of(socialId,0L,0,null);

            SecurityUserDto securityUserDto = new SecurityUserDto();
            securityUserDto.setUserId(userRepository.save(user).getId());
            securityUserDto.setSocialId(socialId);
            securityUserDto.setRole("ROLE_USER");
            securityUserDto.setExist(false);

            return new CustomOAuth2User(securityUserDto);

        }
        else{

            SecurityUserDto securityUserDto = new SecurityUserDto();
            securityUserDto.setUserId(existData.getId());
            securityUserDto.setSocialId(socialId);
            securityUserDto.setRole("ROLE_USER");
            securityUserDto.setExist(true);

            return new CustomOAuth2User(securityUserDto);
        }
    }
}
