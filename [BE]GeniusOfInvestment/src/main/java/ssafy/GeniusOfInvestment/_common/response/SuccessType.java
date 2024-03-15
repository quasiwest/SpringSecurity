package ssafy.GeniusOfInvestment._common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessType {

    //도메인별로 SuccessType 정의, 성공시 msg, data 반환 

    //****************************User****************************//
    //SIGNUP_SUCCESSFULLY("회원 가입 성공"),
    LOGOUT_SUCCESSFULLY("로그아웃 성공"),

    //****************************Room****************************//


    //****************************Game****************************//



    ;
    private final String msg; //success message

}
