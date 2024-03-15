package ssafy.GeniusOfInvestment._common.util;

import org.springframework.security.core.context.SecurityContextHolder;
import ssafy.GeniusOfInvestment.user.dto.SecurityUserDto;

/*
*  Security Context의 인증 객체로부터 다양한 정보를 뽑아서 제공하는 클래스
* */
public abstract class SecurityUtils {

    public static String getUserId() {
        return ((SecurityUserDto)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getSocialId();
    }

    public static SecurityUserDto getUser() {
        return (SecurityUserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
