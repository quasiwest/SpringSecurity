package com.example.oauthjwt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityUserDto {
    private String role;
    private String name;
    private String socialId;
    private boolean isExist;
}
