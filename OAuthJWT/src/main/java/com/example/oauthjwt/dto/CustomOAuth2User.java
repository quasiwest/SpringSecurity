package com.example.oauthjwt.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final SecurityUserDto securityUserDto;

    public CustomOAuth2User(SecurityUserDto securityUserDto){
        this.securityUserDto = securityUserDto;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return securityUserDto.getRole();
            }
        });
        return collection;
    }


    public String getSocialId(){
        return securityUserDto.getSocialId();
    }

    public boolean getExist(){
        return securityUserDto.isExist();
    }

    public Long getUserId(){
        return securityUserDto.getUserId();
    }

    @Override
    public String getName() {
        return securityUserDto.getSocialId();
    }
}
