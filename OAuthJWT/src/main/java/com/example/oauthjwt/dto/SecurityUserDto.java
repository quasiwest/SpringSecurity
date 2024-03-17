package com.example.oauthjwt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityUserDto {
    private Long userId;
    private String role;
    private String socialId;
    private boolean isExist;
}
