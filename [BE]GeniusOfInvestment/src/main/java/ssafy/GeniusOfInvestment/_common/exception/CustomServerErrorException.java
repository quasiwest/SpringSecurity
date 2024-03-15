package ssafy.GeniusOfInvestment._common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ssafy.GeniusOfInvestment._common.response.ErrorType;

@Getter
@RequiredArgsConstructor
public class CustomServerErrorException extends RuntimeException {

    private final ErrorType errorType;
}
