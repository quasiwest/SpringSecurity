package com.example.oauthjwt.oauth2;

import com.example.oauthjwt.dto.CustomOAuth2User;
import com.example.oauthjwt.jwt.JWTUtil;
import com.example.oauthjwt.jwt.SavedToken;
import com.example.oauthjwt.jwt.TokenRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import static com.example.oauthjwt.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.MODE_PARAM_COOKIE_NAME;
import static com.example.oauthjwt.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final TokenRepository tokenRepository;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    public CustomSuccessHandler(JWTUtil jwtUtil, TokenRepository tokenRepository,
            HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository){
        this.jwtUtil = jwtUtil;
        this.tokenRepository = tokenRepository;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        //username 획득
        String username = customUserDetails.getSocialId();

        //role값 획득
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // db에 존재 했는지 판단
        // db에 새로 등록된(회원가입) 회원이라면 닉네임, 프로필 설정 페이지로 리다이렉션
        // db에 있던 회원이면 main 페이지로 리다이렉션
        String targetUrl;


        //토큰 생성
        String access = jwtUtil.createJwt("access", username, role, 600000L); //유효기간 10분
        String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L); //24시간

        targetUrl = determineTargetUrl(request, response, customUserDetails,access);
        // refresh token 저장 로직
        tokenRepository.save(new SavedToken(username,access,refresh));
        
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);

    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
                                        CustomOAuth2User customOAuth2User, String access) {

        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        String mode = CookieUtils.getCookie(request, MODE_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse("");


        if ("login".equalsIgnoreCase(mode)) {

            if (customOAuth2User.getExist()) {
                //회원이 존재하지 않는 경우
                //TODO: 회원가입 페이지(닉네임)로 리다이렉트
                return UriComponentsBuilder.fromUriString(targetUrl)
                        .queryParam("access-token", access)
                        .queryParam("next", "kakaoLogin")
                        .queryParam("user-id", customOAuth2User.getUserId())
                        .build().toUriString();

            } else {
                //회원이 존재하는 경우
                //TODO: 로그인 후 페이지로 리다이렉트
                return UriComponentsBuilder.fromUriString(targetUrl)
                        .queryParam("access-token", access)
                        .queryParam("next", "main")
                        .queryParam("user-id", customOAuth2User.getUserId())
                        .build().toUriString();
            }

        } else if ("unlink".equalsIgnoreCase(mode)) {

            //unlink 로직 추가

            return UriComponentsBuilder.fromUriString(targetUrl)
                    .build().toUriString();
        }

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", "Login failed")
                .build().toUriString();

    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60); //쿠키의 생명주기
        //cookie.setSecure(true);
        //cookie.setPath("/"); //쿠키가 적용될 범위
        cookie.setHttpOnly(true);

        return cookie;
    }
    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {

        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
